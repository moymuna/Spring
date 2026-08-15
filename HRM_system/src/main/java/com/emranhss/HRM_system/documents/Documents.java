package com.emranhss.HRM_system.documents;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documents {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentType documentType;
    private String filePath;
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
