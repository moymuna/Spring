package com.emranhss.HRM_system.office;

import com.emranhss.HRM_system.address.Address;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.project.Project;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.emranhss.HRM_system.notice.Notice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "office")
@AllArgsConstructor
@NoArgsConstructor
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String officeName;

    private String officeCode;


    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "office")
    private List<Department> departments;

    @JsonIgnore
    @OneToMany(mappedBy = "office")
    private List<Employee> employees;

    @JsonIgnore
    @OneToMany(mappedBy = "office")
    private List<Notice> notices;

    @JsonIgnore
    @OneToMany(mappedBy="office")
    private List<Project> projects;

}
