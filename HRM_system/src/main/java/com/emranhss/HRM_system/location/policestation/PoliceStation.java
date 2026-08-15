package com.emranhss.HRM_system.location.policestation;

import com.emranhss.HRM_system.location.district.District;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "policestations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliceStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(length = 50)
    private String name;
    private String nameBn;
    private String postalCode;



    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

}
