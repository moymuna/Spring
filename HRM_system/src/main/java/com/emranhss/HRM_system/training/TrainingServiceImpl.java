package com.emranhss.HRM_system.training;

import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.department.DepartmentRepository;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.TrainingStatus;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    
    @Override
    public TrainingResponseDto createTraining(TrainingRequestDto dto) {

        
        
        Employee employee = null;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Employee not found with id : "
                                    + dto.getEmployeeId()));
        }

        
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id : "
                                + dto.getDepartmentId()));

        
        Training training = TrainingMapper.toEntity(
                dto,
                employee,
                department
        );

        
        Training savedTraining = trainingRepository.save(training);

        
        return TrainingMapper.toResponse(savedTraining);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponseDto> getAllTrainings() {

        return trainingRepository.findAll()
                .stream()
                .map(TrainingMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public TrainingResponseDto getTrainingById(Long id) {

        Training training = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Training not found with id : " + id));

        return TrainingMapper.toResponse(training);
    }
    
    @Override
    public TrainingResponseDto updateTraining(Long id,
                                              TrainingRequestDto dto) {

        
        Training training = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Training not found with id : " + id));

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id : "
                                        + dto.getEmployeeId()));

        
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id : "
                                        + dto.getDepartmentId()));

        
        TrainingMapper.updateEntity(
                training,
                dto,
                employee,
                department
        );

        
        Training updatedTraining = trainingRepository.save(training);

        return TrainingMapper.toResponse(updatedTraining);
    }

    
    @Override
    public void deleteTraining(Long id) {

        
        Training training = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Training not found with id : " + id));

        
        trainingRepository.delete(training);
    }

    @Override
    public TrainingResponseDto approveTraining(Long id) {

        Training training = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Training not found with id : " + id));

        if (training.getStatus() != TrainingStatus.PENDING) {
            throw new ValidationException("Only pending training applications can be approved");
        }

        training.setStatus(TrainingStatus.APPROVED);
        training.setRejectionReason(null);

        Training updated = trainingRepository.save(training);

        return TrainingMapper.toResponse(updated);
    }

    @Override
    public TrainingResponseDto rejectTraining(Long id, String reason) {

        Training training = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Training not found with id : " + id));

        if (training.getStatus() != TrainingStatus.PENDING) {
            throw new ValidationException("Only pending training applications can be rejected");
        }

        training.setStatus(TrainingStatus.REJECTED);
        training.setRejectionReason(reason);

        Training updated = trainingRepository.save(training);

        return TrainingMapper.toResponse(updated);
    }

    @Override
    public TrainingResponseDto applyForTraining(Long id, Long employeeId) {

        Training training = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Training not found with id : " + id));

        if (training.getEmployee() != null) {
            throw new ValidationException("This training has already been claimed by an employee");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : " + employeeId));

        training.setEmployee(employee);
        training.setStatus(TrainingStatus.PENDING);
        training.setRejectionReason(null);

        Training updated = trainingRepository.save(training);

        return TrainingMapper.toResponse(updated);
    }

}
