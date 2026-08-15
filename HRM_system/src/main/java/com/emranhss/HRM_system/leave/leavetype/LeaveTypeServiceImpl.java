package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.exception.ConflictException;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.enums.LeavesType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {
    
    private final LeaveTypeRepository leaveTypeRepository;

    
    @Override
    public LeaveTypeResponseDto saveLeaveType(LeaveTypeRequestDto dto) {

        
        if (leaveTypeRepository.existsByName(dto.getName())) {
            throw new ConflictException("Leave Type already exists.");
        }

        
        LeaveType leaveType = LeaveTypeMapper.toEntity(dto);

        
        LeaveType savedLeaveType = leaveTypeRepository.save(leaveType);

        
        return LeaveTypeMapper.toResponse(savedLeaveType);
    }

    
    @Override
    public LeaveTypeResponseDto getLeaveTypeById(Long id) {

        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave Type not found."));

        return LeaveTypeMapper.toResponse(leaveType);
    }

    
    @Override
    public List<LeaveTypeResponseDto> getAllLeaveTypes() {

        return leaveTypeRepository.findAll()
                .stream()
                .map(LeaveTypeMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public LeaveTypeResponseDto updateLeaveType(Long id,
                                                LeaveTypeRequestDto dto) {

        
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave Type not found."));

        
        leaveTypeRepository.findByName(dto.getName())
                .ifPresent(existing -> {
                    if (existing.getId() != leaveType.getId()) {
                        throw new ConflictException("Leave Type already exists.");
                    }
                });

        
        LeaveTypeMapper.updateEntity(leaveType, dto);

        
        LeaveType updatedLeaveType = leaveTypeRepository.save(leaveType);

        return LeaveTypeMapper.toResponse(updatedLeaveType);
    }

    
    @Override
    public void deleteLeaveType(Long id) {

        
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave Type not found."));

        leaveTypeRepository.delete(leaveType);
    }

    
    @Override
    public LeaveTypeResponseDto getLeaveTypeByName(LeavesType name) {

        LeaveType leaveType = leaveTypeRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave Type not found."));

        return LeaveTypeMapper.toResponse(leaveType);
    }

}
