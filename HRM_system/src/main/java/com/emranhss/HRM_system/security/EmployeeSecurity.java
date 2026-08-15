package com.emranhss.HRM_system.security;

import com.emranhss.HRM_system.advance.AdvanceRepository;
import com.emranhss.HRM_system.applicant.ApplicantRepository;
import com.emranhss.HRM_system.application.ApplicationRepository;
import com.emranhss.HRM_system.attendance.Attendance;
import com.emranhss.HRM_system.attendance.AttendanceRepository;
import com.emranhss.HRM_system.documents.DocumentRepository;
import com.emranhss.HRM_system.documents.Documents;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.Role;
import com.emranhss.HRM_system.leave.leave.LeaveRepository;
import com.emranhss.HRM_system.leave.leavebalance.LeaveBalanceRepository;
import com.emranhss.HRM_system.payroll.PayrollRepository;
import com.emranhss.HRM_system.payslip.PayslipRepository;
import com.emranhss.HRM_system.salary.SalaryRepository;
import com.emranhss.HRM_system.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component("employeeSecurity")
@RequiredArgsConstructor
@Slf4j
public class EmployeeSecurity {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final DocumentRepository documentRepository;
    private final ApplicantRepository applicantRepository;
    private final ApplicationRepository applicationRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final PayslipRepository payslipRepository;
    private final SalaryRepository salaryRepository;
    private final PayrollRepository payrollRepository;
    private final AdvanceRepository advanceRepository;

    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        return null;
    }

    
    public boolean isNotEmployee() {
        User user = getCurrentUser();
        if (user == null) return false;
        return user.getRole() != Role.EMPLOYEE && user.getRole() != Role.APPLICANT;
    }

    
    public boolean isOwnerOrNotEmployee(Long employeeId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        Optional<Employee> employee = employeeRepository.findByUser_Id(user.getId());
        return employee.isPresent() && employee.get().getId().equals(employeeId);
    }

    
    public boolean isOwnerByUserIdOrNotEmployee(Long userId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        return user.getId().equals(userId);
    }

    
    public boolean isOwnerByCodeOrNotEmployee(String employeeCode) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        Optional<Employee> employee = employeeRepository.findByUser_Id(user.getId());
        return employee.isPresent() && employee.get().getEmployeeCode().equals(employeeCode);
    }

    
    public boolean isOwnerByEmailOrNotEmployee(String email) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        return user.getEmail().equals(email);
    }

    
    public boolean isOwnerOrAdmin(Long employeeId) {
        return isOwnerOrNotEmployee(employeeId);
    }

    
    public boolean hasAnyNonEmployeeRole() {
        User user = getCurrentUser();
        if (user == null) return false;
        return user.getRole() != Role.EMPLOYEE && user.getRole() != Role.APPLICANT;
    }

    public boolean isCurrentEmployee(Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) return false;
        return Objects.equals(currentUser.getEmployee().getId(), id) && currentUser.getRole() == Role.EMPLOYEE;
    }

    

    
    public boolean isAddressOwnerOrNotEmployee(Long addressId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        Optional<Employee> currentEmployee = employeeRepository.findByUser_Id(user.getId());
        if (currentEmployee.isEmpty()) return false;

        Employee employee = currentEmployee.get();
        return (employee.getPresentAddress() != null && addressId.equals(employee.getPresentAddress().getId()))
                || (employee.getPermanentAddress() != null && addressId.equals(employee.getPermanentAddress().getId()));
    }
    

    
    public boolean isAttendanceOwnerOrNotEmployee(Long attendanceId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        Optional<Attendance> attendance = attendanceRepository.findById(attendanceId);
        if (attendance.isEmpty()) return false;

        Optional<Employee> currentEmployee = employeeRepository.findByUser_Id(user.getId());
        return currentEmployee.isPresent() &&
                currentEmployee.get().getId().equals(attendance.get().getEmployee().getId());
    }

    

    
    public boolean isDocumentOwnerOrNotEmployee(Long documentId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        Optional<Documents> document = documentRepository.findById(documentId);
        if (document.isEmpty()) return false;

        Optional<Employee> currentEmployee = employeeRepository.findByUser_Id(user.getId());
        return currentEmployee.isPresent() &&
                currentEmployee.get().getId().equals(document.get().getEmployee().getId());
    }

    

    public boolean isLeaveOwnerOrNotEmployee(Long leaveId) {
        return isOwnedRecordOrNotEmployee(leaveId, id -> leaveRepository.findById(id).map(l -> l.getEmployee().getId()));
    }

    
    public boolean canDecideOnLeave(Long leaveId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.HR) {
            return true;
        }
        if (user.getRole() != Role.MANAGER) {
            return false;
        }

        Optional<Employee> currentManager = employeeRepository.findByUser_Id(user.getId());
        if (currentManager.isEmpty()) return false;

        return leaveRepository.findById(leaveId)
                .map(leave -> leave.getEmployee().getManager() != null
                        && leave.getEmployee().getManager().getId().equals(currentManager.get().getId()))
                .orElse(false);
    }

    public boolean isLeaveBalanceOwnerOrNotEmployee(Long leaveBalanceId) {
        return isOwnedRecordOrNotEmployee(leaveBalanceId, id -> leaveBalanceRepository.findById(id).map(lb -> lb.getEmployee().getId()));
    }

    public boolean isPayslipOwnerOrNotEmployee(Long payslipId) {
        return isOwnedRecordOrNotEmployee(payslipId, id -> payslipRepository.findById(id).map(p -> p.getEmployee().getId()));
    }

    public boolean isSalaryOwnerOrNotEmployee(Long salaryId) {
        return isOwnedRecordOrNotEmployee(salaryId, id -> salaryRepository.findById(id).map(s -> s.getEmployee().getId()));
    }

    public boolean isPayrollOwnerOrNotEmployee(Long payrollId) {
        return isOwnedRecordOrNotEmployee(payrollId, id -> payrollRepository.findById(id).map(p -> p.getEmployee().getId()));
    }

    public boolean isAdvanceOwnerOrNotEmployee(Long advanceId) {
        return isOwnedRecordOrNotEmployee(advanceId, id -> advanceRepository.findById(id).map(a -> a.getEmployee().getId()));
    }


    public boolean canDecideOnAdvance(Long advanceId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.HR) {
            return true;
        }
        if (user.getRole() != Role.MANAGER) {
            return false;
        }

        Optional<Employee> currentManager = employeeRepository.findByUser_Id(user.getId());
        if (currentManager.isEmpty()) return false;

        return advanceRepository.findById(advanceId)
                .map(advance -> advance.getEmployee().getManager() != null
                        && advance.getEmployee().getManager().getId().equals(currentManager.get().getId()))
                .orElse(false);
    }

    
    private boolean isOwnedRecordOrNotEmployee(Long recordId, java.util.function.Function<Long, Optional<Long>> ownerEmployeeIdLookup) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.APPLICANT) {
            return false;
        }

        if (user.getRole() != Role.EMPLOYEE) {
            return true;
        }

        Optional<Employee> currentEmployee = employeeRepository.findByUser_Id(user.getId());
        if (currentEmployee.isEmpty()) return false;

        Optional<Long> ownerEmployeeId = ownerEmployeeIdLookup.apply(recordId);
        return ownerEmployeeId.isPresent() && ownerEmployeeId.get().equals(currentEmployee.get().getId());
    }

    

    public boolean isApplicantOwnerOrStaff(Long applicantId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.HR) {
            return true;
        }
        if (user.getRole() != Role.APPLICANT) {
            return false;
        }

        return applicantRepository.findById(applicantId)
                .map(a -> a.getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isApplicantOwnerByEmailOrStaff(String email) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.HR) {
            return true;
        }
        if (user.getRole() != Role.APPLICANT) {
            return false;
        }

        return email != null && email.equals(user.getEmail());
    }

    public boolean isApplicationOwnerOrStaff(Long applicationId) {
        User user = getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.HR) {
            return true;
        }
        if (user.getRole() != Role.APPLICANT) {
            return false;
        }

        return applicationRepository.findById(applicationId)
                .map(a -> a.getApplicant().getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isApplicantOwnerForApplicantIdOrStaff(Long applicantId) {
        return isApplicantOwnerOrStaff(applicantId);
    }

    

    
    public boolean isSelfOrStaff(Long userId) {
        User user = getCurrentUser();
        if (user == null) return false;
        return user.getRole() == Role.ADMIN || user.getRole() == Role.HR || user.getId().equals(userId);
    }

    
    public boolean isSelf(Long userId) {
        User user = getCurrentUser();
        if (user == null) return false;
        return user.getId().equals(userId);
    }
}
