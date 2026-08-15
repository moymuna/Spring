package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.address.Address;
import com.emranhss.HRM_system.address.AddressMapper;
import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.designation.Designation;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.emranhss.HRM_system.office.Office;
import com.emranhss.HRM_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    

    private final AddressMapper addressMapper;

    public Employee toEntity(
            EmployeeRequestDto dto,
            Department department,
            Designation designation,
            Office office,
            Address presentAddress,
            Address permanentAddress,
            User user,
            Employee manager
    ) {

        Employee employee = new Employee();

        employee.setContractNo(dto.getContractNo());
        employee.setJoiningDate(dto.getJoiningDate());

        employee.setStatus(dto.getStatus());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGender(dto.getGender());
        employee.setBloodGroup(dto.getBloodGroup());
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setEmploymentType(dto.getEmploymentType());
        employee.setImage(dto.getImage());

        employee.setDepartment(department);
        employee.setDesignation(designation);
        employee.setOffice(office);

        employee.setPresentAddress(presentAddress);
        employee.setPermanentAddress(permanentAddress);

        employee.setUser(user);
        employee.setManager(manager);

        return employee;
    }

    
    public  void updateEntity(
            Employee employee,
            EmployeeRequestDto dto,
            Department department,
            Designation designation,
            Office office,
            Address presentAddress,
            Address permanentAddress,
            User user,
            Employee manager
    ) {

        employee.setContractNo(dto.getContractNo());
        employee.setJoiningDate(dto.getJoiningDate());

        employee.setStatus(dto.getStatus());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGender(dto.getGender());
        employee.setBloodGroup(dto.getBloodGroup());
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setEmploymentType(dto.getEmploymentType());

        employee.setDepartment(department);
        employee.setDesignation(designation);
        employee.setOffice(office);
        employee.setImage(dto.getImage());

        employee.setPresentAddress(presentAddress);
        employee.setPermanentAddress(permanentAddress);

        employee.setUser(user);
        employee.setManager(manager);
    }

    
    public EmployeeResponseDto toResponse(Employee employee) {

        EmployeeResponseDto dto = new EmployeeResponseDto();

        dto.setId(employee.getId());
        dto.setContractNo(employee.getContractNo());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setDateOfExit(employee.getDateOfExit());
        dto.setStatus(employee.getStatus());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setGender(employee.getGender());
        dto.setBloodGroup(employee.getBloodGroup());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setEmploymentType(employee.getEmploymentType());
        dto.setImage(employee.getImage());

        
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getDepartmentName());
        }

        
        if (employee.getDesignation() != null) {
            dto.setDesignationId(employee.getDesignation().getId());
            dto.setDesignationTitle(employee.getDesignation().getTitle());
        }

        
        if (employee.getOffice() != null) {
            dto.setOfficeId(employee.getOffice().getId());
            dto.setOfficeName(employee.getOffice().getOfficeName());
        }

        
        if (employee.getPresentAddress() != null) {

            dto.setPresentAddress(
                    addressMapper.toResponse(
                            employee.getPresentAddress()
                    )
            );

        }

        
        if (employee.getPermanentAddress() != null) {

            dto.setPermanentAddress(
                    addressMapper.toResponse(
                            employee.getPermanentAddress()
                    )
            );
        }

        
        if (employee.getUser() != null) {

            dto.setUserId(employee.getUser().getId());
            dto.setFullName(employee.getUser().getFullName());
            dto.setEmail(employee.getUser().getEmail());
            dto.setRole(employee.getUser().getRole().name());
        }

        
        if (employee.getManager() != null) {

            dto.setManagerId(employee.getManager().getId());

            if (employee.getManager().getUser() != null) {
                dto.setManagerName(employee.getManager().getUser().getFullName());
            }
        }

        return dto;
    }


}
