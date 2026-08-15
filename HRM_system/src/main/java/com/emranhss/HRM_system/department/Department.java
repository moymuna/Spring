package com.emranhss.HRM_system.department;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.office.Office;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String departmentName;


    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "department_head_id", nullable = true)
    private Employee departmentHead;


    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
