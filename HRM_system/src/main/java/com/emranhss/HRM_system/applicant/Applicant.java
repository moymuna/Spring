package com.emranhss.HRM_system.applicant;

import com.emranhss.HRM_system.enums.EducationLevel;
import com.emranhss.HRM_system.enums.ExperienceLevel;
import com.emranhss.HRM_system.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "applicants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private String address;

    @ElementCollection(targetClass = EducationLevel.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "applicant_education",
            joinColumns = @JoinColumn(name = "applicant_id")
    )
    @Column(name = "education")
    private List<EducationLevel> education;


    @ElementCollection(targetClass = ExperienceLevel.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "applicant_experience",
            joinColumns = @JoinColumn(name = "applicant_id")
    )
    @Column(name = "experience")
    private List<ExperienceLevel> experience;


    private String skills;

    private String cvPath; 

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}
