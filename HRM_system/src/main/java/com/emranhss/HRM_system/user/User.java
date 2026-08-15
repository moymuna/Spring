package com.emranhss.HRM_system.user;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String photoPath;
    private String signaturePath;


    @Column(nullable = false)
    private boolean enabled = true;

    

    @Column(nullable = false)
    private boolean accountLocked = false;

    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    
    @Column(nullable = false)
    private int tokenVersion = 0;

    @OneToOne(mappedBy = "user")
    private Employee employee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
