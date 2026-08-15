package com.emranhss.HRM_system.location.country;

import com.emranhss.HRM_system.location.division.Division;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String countryName;
    private String code;
    private String phoneCode;


    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<Division> divisions;
}
