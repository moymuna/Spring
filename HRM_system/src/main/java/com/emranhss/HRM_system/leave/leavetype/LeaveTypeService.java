package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.enums.LeavesType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaveTypeService {

    
    LeaveTypeResponseDto saveLeaveType(LeaveTypeRequestDto dto);

    
    LeaveTypeResponseDto getLeaveTypeById(Long id);

    
    List<LeaveTypeResponseDto> getAllLeaveTypes();

    
    LeaveTypeResponseDto updateLeaveType(Long id,
                                         LeaveTypeRequestDto dto);

    
    void deleteLeaveType(Long id);

    
    LeaveTypeResponseDto getLeaveTypeByName(LeavesType name);
}
