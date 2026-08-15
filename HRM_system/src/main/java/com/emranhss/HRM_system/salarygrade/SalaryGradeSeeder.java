package com.emranhss.HRM_system.salarygrade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Seeds the 15-step pay scale the first time the application starts so grades
 * 1-15 already carry different salaries. Existing grades are never overwritten —
 * once the admin edits a grade, the seeder leaves it alone.
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class SalaryGradeSeeder implements CommandLineRunner {

    /** Basic salary per grade, index 0 = grade 1 (highest) ... index 14 = grade 15 (lowest). */
    private static final int[] BASIC_BY_GRADE = {
            78000, 66000, 56500, 50000, 43000,
            35500, 29000, 23000, 22000, 16000,
            12500, 11300, 11000, 10200, 9700
    };

    private static final BigDecimal HRA_RATE = new BigDecimal("0.45");
    private static final BigDecimal MEDICAL_RATE = new BigDecimal("0.10");
    private static final BigDecimal CONVEYANCE_RATE = new BigDecimal("0.05");
    private static final BigDecimal PROVIDENT_FUND_RATE = new BigDecimal("0.10");

    private final SalaryGradeRepository salaryGradeRepository;

    @Override
    public void run(String... args) {

        int created = 0;

        for (int index = 0; index < BASIC_BY_GRADE.length; index++) {

            int gradeNumber = index + 1;

            if (salaryGradeRepository.existsByGradeNumber(gradeNumber)) {
                continue;
            }

            BigDecimal basic = BigDecimal.valueOf(BASIC_BY_GRADE[index]);

            SalaryGrade grade = new SalaryGrade();
            grade.setGradeNumber(gradeNumber);
            grade.setTitle("Grade " + gradeNumber);
            grade.setBasicSalary(basic);
            grade.setHra(percentOf(basic, HRA_RATE));
            grade.setMedicalAllowance(percentOf(basic, MEDICAL_RATE));
            grade.setConveyanceAllowance(percentOf(basic, CONVEYANCE_RATE));
            grade.setSpecialAllowance(BigDecimal.ZERO);
            grade.setProvidentFund(percentOf(basic, PROVIDENT_FUND_RATE));
            grade.setProfessionalTax(BigDecimal.ZERO);
            grade.setIncomeTax(BigDecimal.ZERO);
            grade.setActive(true);

            salaryGradeRepository.save(grade);
            created++;
        }

        if (created > 0) {
            log.info("Seeded {} salary grade(s) into the 1-15 pay scale.", created);
        }
    }

    private static BigDecimal percentOf(BigDecimal basic, BigDecimal rate) {
        return basic.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
