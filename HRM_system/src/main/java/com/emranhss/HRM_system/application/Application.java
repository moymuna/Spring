package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.applicant.Applicant;
import com.emranhss.HRM_system.enums.ApplicationStatus;
import com.emranhss.HRM_system.jobpost.JobPost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;


    private Date applyDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    
}
