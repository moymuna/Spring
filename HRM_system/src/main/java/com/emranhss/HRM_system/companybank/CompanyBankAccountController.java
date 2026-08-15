package com.emranhss.HRM_system.companybank;

import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/company-bank")
@RequiredArgsConstructor
public class CompanyBankAccountController {

    private final CompanyBankAccountRepository repository;
    private final AuditLogService auditLogService;

    /** The account salaries are paid from, or null when admin hasn't set one yet. */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public CompanyBankAccount get() {
        return repository.findAll().stream().findFirst().orElse(null);
    }

    /** Creates the account on first save, updates the same row afterwards. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CompanyBankAccount save(@RequestBody CompanyBankAccount dto) {

        if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()
                || dto.getBankName() == null || dto.getBankName().isBlank()
                || dto.getAccountName() == null || dto.getAccountName().isBlank()
                || dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()) {
            throw new ValidationException(
                    "Company name, bank name, account name and account number are required.");
        }

        CompanyBankAccount account = repository.findAll().stream().findFirst()
                .orElseGet(CompanyBankAccount::new);

        account.setCompanyName(dto.getCompanyName());
        account.setBankName(dto.getBankName());
        account.setBankBranch(dto.getBankBranch());
        account.setAccountName(dto.getAccountName());
        account.setAccountNumber(dto.getAccountNumber());
        account.setUpdatedAt(LocalDateTime.now());

        CompanyBankAccount saved = repository.save(account);

        auditLogService.record("CompanyBankAccount", saved.getId(), AuditAction.UPDATE,
                "Company salary account set to " + saved.getBankName());

        return saved;
    }
}
