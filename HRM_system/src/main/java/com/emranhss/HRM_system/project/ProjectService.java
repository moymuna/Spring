package com.emranhss.HRM_system.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {

    
    ProjectResponseDto createProject(ProjectRequestDto dto);

    
    List<ProjectResponseDto> getAllProjects();

    
    ProjectResponseDto getProjectById(Long id);

    
    ProjectResponseDto updateProject(Long id,
                                     ProjectRequestDto dto);

    
    void deleteProject(Long id);


    List<ProjectResponseDto>getByEmployeeId(Long id);

}

