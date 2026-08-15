package com.emranhss.HRM_system.training;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrainingService {

    
    TrainingResponseDto createTraining(TrainingRequestDto dto);

    
    List<TrainingResponseDto> getAllTrainings();

    
    TrainingResponseDto getTrainingById(Long id);

    
    TrainingResponseDto updateTraining(Long id,
                                       TrainingRequestDto dto);

    
    void deleteTraining(Long id);

    
    TrainingResponseDto approveTraining(Long id);

    
    TrainingResponseDto rejectTraining(Long id, String reason);

    
    TrainingResponseDto applyForTraining(Long id, Long employeeId);
}
