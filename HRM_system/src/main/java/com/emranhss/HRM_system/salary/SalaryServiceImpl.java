package com.emranhss.HRM_system.salary;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.salarygrade.SalaryGrade;
import com.emranhss.HRM_system.salarygrade.SalaryGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryGradeRepository salaryGradeRepository;
    private final AuditLogService auditLogService;


    @Override
    public SalaryResponseDto createSalary(SalaryRequestDto dto) {


        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : "
                                        + dto.getEmployeeId()));

        SalaryGrade grade = resolveGrade(dto.getSalaryGradeId());


        Salary salary = SalaryMapper.toEntity(dto, employee, grade);


        Salary savedSalary = salaryRepository.save(salary);

        auditLogService.record("Salary", savedSalary.getId(), AuditAction.CREATE,
                "Salary structure created for employee " + dto.getEmployeeId()
                        + (grade == null ? "" : " on grade " + grade.getGradeNumber()));

        return SalaryMapper.toResponse(savedSalary);
    }


    @Override
    public List<SalaryResponseDto> getAllSalaries() {

        return salaryRepository.findAll()
                .stream()
                .map(SalaryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SalaryResponseDto getSalaryById(Long id) {


        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salary not found with id : " + id));

        return SalaryMapper.toResponse(salary);
    }

    /** The employee's own salary structure — the active one if there is one. */
    @Override
    public SalaryResponseDto getSalaryByEmployee(Long employeeId) {

        Salary salary = salaryRepository.findByEmployeeIdAndActiveTrue(employeeId)
                .orElseGet(() -> salaryRepository.findByEmployeeId(employeeId)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No salary structure found for employee " + employeeId)));

        return SalaryMapper.toResponse(salary);
    }

    @Override
    public List<SalaryResponseDto> getSalaryHistoryByEmployee(Long employeeId) {

        return salaryRepository.findByEmployeeId(employeeId)
                .stream()
                .map(SalaryMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public SalaryResponseDto updateSalary(Long id,
                                          SalaryRequestDto dto) {


        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salary not found with id : " + id));


        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : "
                                        + dto.getEmployeeId()));

        SalaryGrade grade = resolveGrade(dto.getSalaryGradeId());


        SalaryMapper.updateEntity(
                salary,
                dto,
                employee,
                grade
        );


        Salary updatedSalary = salaryRepository.save(salary);

        auditLogService.record("Salary", updatedSalary.getId(), AuditAction.UPDATE,
                "Salary structure updated for employee " + dto.getEmployeeId()
                        + (grade == null ? "" : " on grade " + grade.getGradeNumber()));

        return SalaryMapper.toResponse(updatedSalary);
    }


    @Override
    public void deleteSalary(Long id) {


        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salary not found with id : " + id));


        salaryRepository.delete(salary);

        auditLogService.record("Salary", id, AuditAction.DELETE, "Salary structure deleted");
    }

    private SalaryGrade resolveGrade(Long salaryGradeId) {

        if (salaryGradeId == null) {
            return null;
        }

        return salaryGradeRepository.findById(salaryGradeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salary grade not found with id : " + salaryGradeId));
    }


}
