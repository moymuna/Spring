package com.emranhss.HRM_system.notice;

import com.emranhss.HRM_system.office.Office;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "notice")
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String description;

    private Date publishDate;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;


}
