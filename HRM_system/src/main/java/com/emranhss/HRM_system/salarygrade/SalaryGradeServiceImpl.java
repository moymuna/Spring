package com.emranhss.HRM_system.salarygrade;

import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.exception.ConflictException;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryGradeServiceImpl implements SalaryGradeService {

    /**
     * Grades 1-15 are seeded as the default scale, but the scale is not fixed at 15 —
     * an admin can add further steps. Only the lower bound is enforced.
     */
    public static final int MIN_GRADE = 1;

    private final SalaryGradeRepository salaryGradeRepository;
    private final AuditLogService auditLogService;

    @Override
    public SalaryGradeResponseDto createGrade(SalaryGradeRequestDto dto) {

        validateGradeNumber(dto.getGradeNumber());

        if (salaryGradeRepository.existsByGradeNumber(dto.getGradeNumber())) {
            throw new ConflictException("Grade " + dto.getGradeNumber() + " already exists.");
        }

        SalaryGrade grade = salaryGradeRepository.save(SalaryGradeMapper.toEntity(dto));

        auditLogService.record("SalaryGrade", grade.getId(), AuditAction.CREATE,
                "Salary grade " + grade.getGradeNumber() + " created");

        return SalaryGradeMapper.toResponse(grade);
    }

    @Override
    public List<SalaryGradeResponseDto> getAllGrades() {

        return salaryGradeRepository.findAllByOrderByGradeNumberAsc()
                .stream()
                .map(SalaryGradeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryGradeResponseDto> getActiveGrades() {

        return salaryGradeRepository.findByActiveTrueOrderByGradeNumberAsc()
                .stream()
                .map(SalaryGradeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SalaryGradeResponseDto getGradeById(Long id) {

        return SalaryGradeMapper.toResponse(findGrade(id));
    }

    @Override
    public SalaryGradeResponseDto getGradeByNumber(Integer gradeNumber) {

        SalaryGrade grade = salaryGradeRepository.findByGradeNumber(gradeNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Salary grade " + gradeNumber + " not found."));

        return SalaryGradeMapper.toResponse(grade);
    }

    @Override
    public SalaryGradeResponseDto updateGrade(Long id, SalaryGradeRequestDto dto) {

        SalaryGrade grade = findGrade(id);

        validateGradeNumber(dto.getGradeNumber());

        salaryGradeRepository.findByGradeNumber(dto.getGradeNumber())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Grade " + dto.getGradeNumber() + " already exists.");
                });

        SalaryGradeMapper.updateEntity(grade, dto);

        SalaryGrade updated = salaryGradeRepository.save(grade);

        auditLogService.record("SalaryGrade", updated.getId(), AuditAction.UPDATE,
                "Salary grade " + updated.getGradeNumber() + " updated");

        return SalaryGradeMapper.toResponse(updated);
    }

    @Override
    public void deleteGrade(Long id) {

        SalaryGrade grade = findGrade(id);

        salaryGradeRepository.delete(grade);

        auditLogService.record("SalaryGrade", id, AuditAction.DELETE,
                "Salary grade " + grade.getGradeNumber() + " deleted");
    }

    private SalaryGrade findGrade(Long id) {

        return salaryGradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary grade not found with id : " + id));
    }

    private void validateGradeNumber(Integer gradeNumber) {

        if (gradeNumber == null || gradeNumber < MIN_GRADE) {
            throw new ValidationException("Grade number must be " + MIN_GRADE + " or higher.");
        }
    }
}
