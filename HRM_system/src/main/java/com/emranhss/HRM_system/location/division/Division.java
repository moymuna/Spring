package com.emranhss.HRM_system.location.division;

import com.emranhss.HRM_system.location.country.Country;
import com.emranhss.HRM_system.location.district.District;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "divisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private  String nameBN;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @JsonIgnore
    @OneToMany(mappedBy = "division")
    private List<District> districts;

}
