package com.emranhss.HRM_system.location.district;

import com.emranhss.HRM_system.location.division.Division;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "districts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String districtsName;
    private String nameBN;
    private String districtCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;

    @JsonIgnore
    @OneToMany(mappedBy = "district")
    private List<PoliceStation> policeStations;
}
