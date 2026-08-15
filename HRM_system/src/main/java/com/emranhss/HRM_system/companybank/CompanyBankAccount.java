package com.emranhss.HRM_system.companybank;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The company's own bank account that salaries are paid out of. Single-company
 * system, so in practice this table holds one row that admin keeps up to date.
 */
@Entity
@Table(name = "company_bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String companyName;

    @Column(nullable = false, length = 100)
    private String bankName;

    @Column(length = 100)
    private String bankBranch;

    @Column(nullable = false, length = 100)
    private String accountName;

    @Column(nullable = false, length = 40)
    private String accountNumber;

    private LocalDateTime updatedAt;
}
