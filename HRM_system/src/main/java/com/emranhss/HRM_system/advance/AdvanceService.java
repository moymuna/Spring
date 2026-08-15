package com.emranhss.HRM_system.advance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AdvanceService {

    AdvanceResponseDto saveAdvance(AdvanceRequestDto dto);

    AdvanceResponseDto getAdvanceById(Long id);

    List<AdvanceResponseDto> getAllAdvances();

    List<AdvanceResponseDto> getAdvancesByEmployee(Long employeeId);

    List<AdvanceResponseDto> getAdvancesByStatus(String status);

    AdvanceResponseDto updateAdvance(Long id, AdvanceRequestDto dto);

    void deleteAdvance(Long id);

    AdvanceResponseDto approveAdvance(Long advanceId);

    AdvanceResponseDto rejectAdvance(Long advanceId, String rejectionReason);

    AdvanceResponseDto markAsPaid(Long advanceId);

    AdvanceResponseDto recordRecovery(Long advanceId, BigDecimal amount);

    List<AdvanceResponseDto> searchAdvances(String keyword);

    long getAdvanceCount();

    long getPendingCount();

    Page<AdvanceResponseDto> getAdvances(Pageable pageable);
}
