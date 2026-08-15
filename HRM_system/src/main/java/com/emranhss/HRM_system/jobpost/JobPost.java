package com.emranhss.HRM_system.jobpost;

import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "job_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String requirements;

    private String location;

    private Double minSalary;
    private Double maxSalary;


    private Date postedDate;


    private Date deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status; 

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
