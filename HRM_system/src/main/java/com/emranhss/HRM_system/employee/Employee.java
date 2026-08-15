package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.address.Address;
import com.emranhss.HRM_system.attendance.Attendance;
import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.designation.Designation;
import com.emranhss.HRM_system.documents.Documents;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import com.emranhss.HRM_system.interview.Interview;
import com.emranhss.HRM_system.leave.leave.Leave;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.emranhss.HRM_system.office.Office;
import com.emranhss.HRM_system.payroll.Payroll;
import com.emranhss.HRM_system.payslip.Payslip;
import com.emranhss.HRM_system.performancereview.PerformanceReview;
import com.emranhss.HRM_system.project.Project;
import com.emranhss.HRM_system.salary.Salary;
import com.emranhss.HRM_system.training.Training;
import com.emranhss.HRM_system.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Lazy;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String contractNo;


    @Column(nullable = false)
    private Date joiningDate;

    private Date dateOfExit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmployeeStatus status;
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
    private String bloodGroup;

    private String image;

    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentType employmentType;

    /* Salary account: where this employee's net pay is transferred each month. */
    @Column(length = 100)
    private String bankName;

    @Column(length = 100)
    private String bankBranch;

    @Column(length = 100)
    private String bankAccountName;

    @Column(length = 40)
    private String bankAccountNumber;


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_id")
    private Designation designation;

    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private Office office;

    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "present_address_id")
    private Address presentAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permanent_address_id")
    private Address permanentAddress;

    

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    
    @JsonIgnore
    @OneToOne(mappedBy = "employee")
    private Salary salary;

    

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Attendance> attendances;

    

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Leave> leaves;

    

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Payroll> payrolls;

    

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Documents> documents;

    

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Training> trainings;

    

    @JsonIgnore
    @ManyToMany(mappedBy = "employee")
    private List<Project> projects;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private Set<Employee> directReports = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<PerformanceReview> performanceReviews;

    @JsonIgnore
    @OneToMany(mappedBy = "reviewer")
    private List<PerformanceReview> reviewsGiven;

    @OneToOne(mappedBy = "departmentHead")
    private Department headedDepartment;

    @OneToMany(mappedBy = "employee")
    private List<Payslip> payslips;




}