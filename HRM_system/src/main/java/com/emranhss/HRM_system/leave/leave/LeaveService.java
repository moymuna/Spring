package com.emranhss.HRM_system.leave.leave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface LeaveService {
    
    LeaveResponseDto saveLeave(LeaveRequestDto dto);

    
    LeaveResponseDto getLeaveById(Long id);

    
    List<LeaveResponseDto> getAllLeaves();

    
    LeaveResponseDto updateLeave(Long id, LeaveRequestDto dto);

    
    void deleteLeave(Long id);

    
    List<LeaveResponseDto> getLeavesByEmployee(Long employeeId);

    
    List<LeaveResponseDto> getLeavesByStatus(String status);

    
    List<LeaveResponseDto> getLeavesBetweenDates(LocalDate startDate,
                                                 LocalDate endDate);
    
    LeaveResponseDto approveLeave(Long leaveId);

    
    LeaveResponseDto rejectLeave(Long leaveId, String rejectionReason);

    LeaveResponseDto cancelLeave(Long leaveId);

    List<LeaveResponseDto> searchLeaves(String keyword);

    long getLeaveCount();

    Page<LeaveResponseDto> getLeaves(Pageable pageable);
}
