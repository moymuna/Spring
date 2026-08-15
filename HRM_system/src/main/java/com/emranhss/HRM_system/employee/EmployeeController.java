package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeResponseDto> saveEmployee(
            @RequestPart("employee") String employeeJson, @RequestPart(value = "image", required = false) MultipartFile image) {

        ObjectMapper mapper = new ObjectMapper();
        EmployeeRequestDto dto = mapper.readValue(employeeJson, EmployeeRequestDto.class);

        return new ResponseEntity<>(
                employeeService.saveEmployee(dto, image),
                HttpStatus.CREATED
        );

    }




    
    
    

    /** Promotes a hired applicant into an employee, reusing their existing account. */
    @PostMapping("/hire/{applicationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeResponseDto> hireApplicant(
            @PathVariable Long applicationId,
            @RequestBody HireRequestDto dto) {

        return new ResponseEntity<>(
                employeeService.hireApplicant(applicationId, dto),
                HttpStatus.CREATED
        );
    }




    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#id)")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(

            @PathVariable Long id,

            @RequestPart("employee") EmployeeRequestDto dto,

            @RequestPart(value = "image", required = false)
            MultipartFile image) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, dto, image)
        );
    }




    
    
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Long id) {


        employeeService.deleteEmployee(id);


        return ResponseEntity.ok(
                "Employee deleted successfully"
        );

    }




    
    
    

    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#id)")
    public ResponseEntity<EmployeeResponseDto> getById(
            @PathVariable Long id) {


        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );

    }




    
    
    

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees(){


        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );

    }




    
    
    

    @GetMapping("/code/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<EmployeeResponseDto> getByCode(
            @PathVariable String employeeCode){


        return ResponseEntity.ok(
                employeeService.getEmployeeByCode(employeeCode)
        );

    }




    
    
    

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<EmployeeResponseDto> getByEmail(
            @PathVariable String email){


        return ResponseEntity.ok(
                employeeService.getEmployeeByEmail(email)
        );

    }




    
    
    

    @GetMapping("/user/{userId}")
    @PreAuthorize("@employeeSecurity.isOwnerByUserIdOrNotEmployee(#userId)")
    public ResponseEntity<EmployeeResponseDto> getByUserId(
            @PathVariable Long userId){


        return ResponseEntity.ok(
                employeeService.getEmployeeByUserId(userId)
        );

    }




    
    
    

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byDepartment(
            @PathVariable Long departmentId){


        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(departmentId)
        );

    }




    
    
    

    @GetMapping("/designation/{designationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byDesignation(
            @PathVariable Long designationId){


        return ResponseEntity.ok(
                employeeService.getEmployeesByDesignation(designationId)
        );

    }




    
    
    

    @GetMapping("/office/{officeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byOffice(
            @PathVariable Long officeId){


        return ResponseEntity.ok(
                employeeService.getEmployeesByOffice(officeId)
        );

    }




    
    
    

    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byManager(
            @PathVariable Long managerId){


        return ResponseEntity.ok(
                employeeService.getEmployeesByManager(managerId)
        );

    }




    
    
    

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byStatus(
            @PathVariable EmployeeStatus status){


        return ResponseEntity.ok(
                employeeService.getEmployeesByStatus(status)
        );

    }




    
    
    

    @GetMapping("/employment-type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byEmploymentType(
            @PathVariable EmploymentType type){


        return ResponseEntity.ok(
                employeeService.getEmployeesByEmploymentType(type)
        );

    }




    
    
    

    @GetMapping("/gender/{gender}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> byGender(
            @PathVariable Gender gender){


        return ResponseEntity.ok(
                employeeService.getEmployeesByGender(gender)
        );

    }




    
    
    

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> search(
            @RequestParam String keyword){


        return ResponseEntity.ok(
                employeeService.searchEmployees(keyword)
        );

    }




    
    
    

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR','MANAGER')")
    public ResponseEntity<Long> count(){


        return ResponseEntity.ok(
                employeeService.getEmployeeCount()
        );

    }




    
    
    

    @GetMapping("/count/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR','MANAGER')")
    public ResponseEntity<Long> activeCount(){


        return ResponseEntity.ok(
                employeeService.getActiveEmployeeCount()
        );

    }




    
    
    

    @GetMapping("/count/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR','MANAGER')")
    public ResponseEntity<Long> inactiveCount(){


        return ResponseEntity.ok(
                employeeService.getInactiveEmployeeCount()
        );

    }




    
    
    

    @GetMapping("/stats/by-department")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<java.util.Map<String, Long>> headcountByDepartment() {
        return ResponseEntity.ok(employeeService.getActiveHeadcountByDepartment());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Page<EmployeeResponseDto>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){


        Pageable pageable =
                PageRequest.of(page,size);


        return ResponseEntity.ok(
                employeeService.getEmployees(pageable)
        );

    }


}
