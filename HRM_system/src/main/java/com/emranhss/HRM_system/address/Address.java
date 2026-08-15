package com.emranhss.HRM_system.address;


import com.emranhss.HRM_system.location.country.Country;
import com.emranhss.HRM_system.location.division.Division;
import com.emranhss.HRM_system.location.district.District;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String addressLine1;

    
    private String addressLine2;


    private String postOffice;

    private String postalCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "police_station_id")
    private PoliceStation policeStation;


}
