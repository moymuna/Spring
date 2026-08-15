package com.emranhss.HRM_system.interview;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;


import com.emranhss.HRM_system.application.Application;
import com.emranhss.HRM_system.application.ApplicationRepository;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.notification.NotificationService;
import com.emranhss.HRM_system.user.User;
import com.emranhss.HRM_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;









    @Override
    public InterviewResponseDto  scheduleInterview(InterviewRequestDto dto) {

        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        User interviewer = userRepository.findById(dto.getInterviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Interview interview = InterviewMapper.toEntity(dto, application, interviewer);

        Interview saved = interviewRepository.save(interview);

        notificationService.notify(interviewer,
                "You have been scheduled to interview " + application.getApplicant().getName()
                        + " on " + dto.getInterviewDate() + ".",
                "Interview", saved.getId());

        return InterviewMapper.toDTO(saved);
    }

    @Override
    public InterviewResponseDto getById(Long id) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        return InterviewMapper.toDTO(interview);
    }

    @Override
    public List<InterviewResponseDto> getAll() {

        return interviewRepository.findAll()
                .stream()
                .map(InterviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponseDto> getByApplication(Long applicationId) {

        return interviewRepository.findByApplication_Id(applicationId)
                .stream()
                .map(InterviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponseDto> getByInterviewer(Long interviewerId) {

        return interviewRepository.findByInterviewer_Id(interviewerId)
                .stream()
                .map(InterviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InterviewResponseDto updateInterview(Long id, InterviewRequestDto dto) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

     User interviewer = userRepository.findById(dto.getInterviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        interview.setApplication(application);
        interview.setInterviewer(interviewer);
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setFeedback(dto.getFeedback());
        interview.setResult(dto.getResult());

        return InterviewMapper.toDTO(interviewRepository.save(interview));
    }

    @Override
    public void delete(Long id) {
        interviewRepository.deleteById(id);
    }

}
