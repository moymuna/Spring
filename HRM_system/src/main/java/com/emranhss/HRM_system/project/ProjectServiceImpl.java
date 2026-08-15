package com.emranhss.HRM_system.project;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.office.Office;
import com.emranhss.HRM_system.office.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository;

    
    @Override
    public ProjectResponseDto createProject(ProjectRequestDto dto) {

        
        List<Employee> employees =
                employeeRepository.findAllById(dto.getEmployeeId());

        
        Office office =
                officeRepository.findById(dto.getOfficeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Office not found with id : "
                                                + dto.getOfficeId()
                                )
                        );


        
        Project project =
                ProjectMapper.toEntity(dto,employees,office);

        
        Project savedProject =
                projectRepository.save(project);

        return ProjectMapper.toResponse(savedProject);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long id) {

        
        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found with id : " + id));

        return ProjectMapper.toResponse(project);
    }

    
    @Override
    public ProjectResponseDto updateProject(Long id,
                                            ProjectRequestDto dto) {

        
        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found with id : " + id));

        
        List<Employee> employees =
                employeeRepository.findAllById(dto.getEmployeeId());

        Office office =
                officeRepository.findById(
                                dto.getOfficeId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Office not found with id : "
                                                + dto.getOfficeId()
                                )
                        );

        
ProjectMapper.updateEntity(
                project,
                dto,
                employees,
                office
        );

        
        Project updatedProject =
                projectRepository.save(project);

        return ProjectMapper.toResponse(updatedProject);
    }

    
    @Override
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found with id : " + id));

        projectRepository.delete(project);
    }

    @Override
    public List<ProjectResponseDto> getByEmployeeId(Long id) {

        return projectRepository.findProjectByEmployeeId(id).stream().map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }
}
