package com.emranhss.HRM_system.interview;

import com.emranhss.HRM_system.application.Application;
import com.emranhss.HRM_system.enums.InterviewStatus;
import com.emranhss.HRM_system.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private User interviewer;


    private Date interviewDate;

    @Column(length = 2000)
    private String feedback;

    @Enumerated(EnumType.STRING)
    private InterviewStatus result;
    
}
