package com.emranhss.HRM_system.interview;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public InterviewResponseDto schedule(@RequestBody InterviewRequestDto dto) {
        return interviewService.scheduleInterview(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public InterviewResponseDto getById(@PathVariable Long id) {
        return interviewService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<InterviewResponseDto> getAll() {
        return interviewService.getAll();
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<InterviewResponseDto> getByApplication(@PathVariable Long applicationId) {
        return interviewService.getByApplication(applicationId);
    }

    @GetMapping("/interviewer/{interviewerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<InterviewResponseDto> getByInterviewer(@PathVariable Long interviewerId) {
        return interviewService.getByInterviewer(interviewerId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public InterviewResponseDto update(@PathVariable Long id,
                                       @RequestBody InterviewRequestDto dto) {
        return interviewService.updateInterview(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public void delete(@PathVariable Long id) {
        interviewService.delete(id);
    }
}
