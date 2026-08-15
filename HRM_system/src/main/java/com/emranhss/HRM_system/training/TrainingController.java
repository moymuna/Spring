package com.emranhss.HRM_system.training;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<TrainingResponseDto> createTraining(
            @RequestBody TrainingRequestDto dto) {

        TrainingResponseDto response =
                trainingService.createTraining(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<TrainingResponseDto>> getAllTrainings() {

        return ResponseEntity.ok(
                trainingService.getAllTrainings()
        );
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<TrainingResponseDto> getTrainingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                trainingService.getTrainingById(id)
        );
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<TrainingResponseDto> updateTraining(
            @PathVariable Long id,
            @RequestBody TrainingRequestDto dto) {

        TrainingResponseDto response =
                trainingService.updateTraining(id, dto);

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<String> deleteTraining(
            @PathVariable Long id) {

        trainingService.deleteTraining(id);

        return ResponseEntity.ok("Training deleted successfully.");
    }

    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<TrainingResponseDto> approveTraining(
            @PathVariable Long id) {

        return ResponseEntity.ok(trainingService.approveTraining(id));
    }

    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<TrainingResponseDto> rejectTraining(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {

        return ResponseEntity.ok(trainingService.rejectTraining(id, reason));
    }

    
    @PutMapping("/{id}/apply")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER') or @employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public ResponseEntity<TrainingResponseDto> applyForTraining(
            @PathVariable Long id,
            @RequestParam Long employeeId) {

        return ResponseEntity.ok(trainingService.applyForTraining(id, employeeId));
    }
}
