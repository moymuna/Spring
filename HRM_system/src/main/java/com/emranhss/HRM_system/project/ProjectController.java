package com.emranhss.HRM_system.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR', 'MANAGER')")
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestBody ProjectRequestDto dto) {

        ProjectResponseDto response =
                projectService.createProject(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'HR')")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {

        return ResponseEntity.ok(
                projectService.getAllProjects()
        );
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<ProjectResponseDto> getProjectById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                projectService.getProjectById(id)
        );
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestDto dto) {

        ProjectResponseDto response =
                projectService.updateProject(id, dto);

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> deleteProject(
            @PathVariable Long id) {

        projectService.deleteProject(id);

        return ResponseEntity.ok("Project deleted successfully.");
    }

    @GetMapping("/employee/{id}/projects")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectResponseDto>> getByEmployeeId(@PathVariable Long id){
        return ResponseEntity.ok(projectService.getByEmployeeId(id));
    }
}
