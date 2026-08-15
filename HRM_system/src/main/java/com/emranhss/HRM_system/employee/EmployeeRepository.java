package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {



    Optional<Employee> findByUserId(Long userId);

    Optional<Employee> findByUserEmail(String email);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    boolean existsByUserId(Long userId);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByUserEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByDesignationId(Long designationId);

    List<Employee> findByOfficeId(Long officeId);

    List<Employee> findByManagerId(Long managerId);

    List<Employee> findByStatus(EmployeeStatus status);

    List<Employee> findByEmploymentType(EmploymentType employmentType);






    Optional<Employee> findByUser_Email(String email);



    Optional<Employee> findByUser_Id(Long userId);



    List<Employee> findByDepartment_Id(Long departmentId);



    List<Employee> findByDesignation_Id(Long designationId);



    List<Employee> findByOffice_Id(Long officeId);



    List<Employee> findByManager_Id(Long managerId);







    List<Employee> findByGender(Gender gender);



    long countByStatus(EmployeeStatus status);




    @Query("""
            SELECT e FROM Employee e
            WHERE
            LOWER(e.employeeCode) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(e.user.fullName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(e.user.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Employee> searchEmployees(String keyword);

    @Query("""
            SELECT e.department.departmentName, COUNT(e)
            FROM Employee e
            WHERE e.status = com.emranhss.HRM_system.enums.EmployeeStatus.ACTIVE
            GROUP BY e.department.departmentName
            """)
    List<Object[]> countActiveEmployeesByDepartment();

}
