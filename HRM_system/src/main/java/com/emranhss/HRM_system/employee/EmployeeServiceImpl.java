package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.exception.ConflictException;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;

import com.emranhss.HRM_system.address.*;
import com.emranhss.HRM_system.applicant.Applicant;
import com.emranhss.HRM_system.application.Application;
import com.emranhss.HRM_system.application.ApplicationRepository;
import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.enums.ApplicationStatus;
import com.emranhss.HRM_system.notification.NotificationService;
import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.department.DepartmentRepository;
import com.emranhss.HRM_system.designation.Designation;
import com.emranhss.HRM_system.designation.DesignationRepository;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import com.emranhss.HRM_system.enums.Role;
import com.emranhss.HRM_system.location.country.CountryRepository;
import com.emranhss.HRM_system.location.district.DistrictRepository;
import com.emranhss.HRM_system.location.division.DivisionRepository;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.emranhss.HRM_system.location.policestation.PoliceStationRepository;
import com.emranhss.HRM_system.office.Office;
import com.emranhss.HRM_system.office.OfficeRepository;
import com.emranhss.HRM_system.utill.FileStorageService;
import com.emranhss.HRM_system.user.User;
import com.emranhss.HRM_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final OfficeRepository officeRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final DivisionRepository divisionRepository;
    private final DistrictRepository districtRepository;
    private final PoliceStationRepository policeStationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper  addressMapper;
    private final AuditLogService auditLogService;
    private final FileStorageService fileStorageService;
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;
    private final com.emranhss.HRM_system.leave.leavebalance.LeaveBalanceService leaveBalanceService;

    
    @Override
    @Transactional
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto dto, MultipartFile image) {

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());



        userRepository.save(user);

        Employee employee = new Employee();

        employee.setUser(user);
        employee.setEmployeeCode(
                dto.getEmployeeCode() != null && !dto.getEmployeeCode().isBlank()
                        ? dto.getEmployeeCode()
                        : generateEmployeeCode()
        );
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setEmploymentType(dto.getEmploymentType());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setBloodGroup(dto.getBloodGroup());
        employee.setContractNo(dto.getContractNo());
        employee.setGender(dto.getGender());
        employee.setBankName(dto.getBankName());
        employee.setBankBranch(dto.getBankBranch());
        employee.setBankAccountName(dto.getBankAccountName());
        employee.setBankAccountNumber(dto.getBankAccountNumber());



        if (image != null && !image.isEmpty()) {
            employee.setImage(uploadImage(image, dto.getFullName()));
        }

        employee.setStatus(
                dto.getStatus() != null ? dto.getStatus() : EmployeeStatus.ACTIVE);

        if (dto.getDepartmentId() != null) {
            employee.setDepartment(
                    departmentRepository.findById(dto.getDepartmentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        }

        if (dto.getDesignationId() != null) {
            employee.setDesignation(
                    designationRepository.findById(dto.getDesignationId())
                            .orElseThrow(() -> new ResourceNotFoundException("Designation not found")));
        }

        
        if (dto.getOfficeId() != null) {
            employee.setOffice(
                    officeRepository.findById(dto.getOfficeId())
                            .orElseThrow(() -> new ResourceNotFoundException("Office not found")));
        }


        if (dto.getPresentAddress() != null) {

            Address presentAddress = mapAddress(dto.getPresentAddress());

            employee.setPresentAddress(
                    addressRepository.save(presentAddress)
            );
        }


        if (dto.getPermanentAddress() != null) {

            Address permanentAddress = mapAddress(dto.getPermanentAddress());

            employee.setPermanentAddress(
                    addressRepository.save(permanentAddress)
            );
        }

        if (dto.getManagerId() != null) {
            employee.setManager(
                    employeeRepository.findById(dto.getManagerId())
                            .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
        }

        Employee saved = employeeRepository.save(employee);

        leaveBalanceService.createBalancesForNewEmployee(saved);

        auditLogService.record("Employee", saved.getId(), AuditAction.CREATE,
                "Employee " + saved.getEmployeeCode() + " created");

        return mapToResponse(saved);
    }



    /**
     * Turns a hired applicant into an employee.
     *
     * The applicant already signed up, so their existing user account is reused and
     * promoted from APPLICANT to EMPLOYEE rather than a second account being created.
     * Name, email, phone and address carry over from the applicant record; HR only
     * supplies the employment terms.
     */
    @Override
    @Transactional
    public EmployeeResponseDto hireApplicant(Long applicationId, HireRequestDto dto) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        Applicant applicant = application.getApplicant();
        if (applicant == null) {
            throw new ValidationException("This application has no applicant attached.");
        }

        if (application.getStatus() == ApplicationStatus.HIRED) {
            throw new ConflictException("This application has already been hired.");
        }

        if (application.getStatus() == ApplicationStatus.REJECTED) {
            throw new ValidationException("A rejected application cannot be hired.");
        }

        User user = applicant.getUser();
        if (user == null) {
            throw new ValidationException("This applicant has no user account to promote.");
        }

        if (employeeRepository.findByUser_Id(user.getId()).isPresent()) {
            throw new ConflictException("This applicant is already an employee.");
        }

        user.setRole(Role.EMPLOYEE);
        userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setEmployeeCode(
                dto.getEmployeeCode() != null && !dto.getEmployeeCode().isBlank()
                        ? dto.getEmployeeCode()
                        : generateEmployeeCode()
        );
        employee.setJoiningDate(dto.getJoiningDate() != null ? dto.getJoiningDate() : new java.util.Date());
        employee.setEmploymentType(
                dto.getEmploymentType() != null ? dto.getEmploymentType() : EmploymentType.FULL_TIME);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setContractNo(dto.getContractNo());

        if (dto.getDepartmentId() != null) {
            employee.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        }

        if (dto.getDesignationId() != null) {
            employee.setDesignation(designationRepository.findById(dto.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found")));
        }

        if (dto.getOfficeId() != null) {
            employee.setOffice(officeRepository.findById(dto.getOfficeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Office not found")));
        }

        if (dto.getManagerId() != null) {
            employee.setManager(employeeRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
        }

        // The applicant keeps a single free-text address; carry it into line 1 so HR
        // can complete the structured fields afterwards instead of retyping it.
        if (applicant.getAddress() != null && !applicant.getAddress().isBlank()) {
            Address address = new Address();
            address.setAddressLine1(applicant.getAddress());
            employee.setPresentAddress(addressRepository.save(address));
        }

        Employee saved = employeeRepository.save(employee);

        leaveBalanceService.createBalancesForNewEmployee(saved);

        application.setStatus(ApplicationStatus.HIRED);
        applicationRepository.save(application);

        auditLogService.record("Employee", saved.getId(), AuditAction.CREATE,
                "Hired applicant " + applicant.getName() + " as employee " + saved.getEmployeeCode()
                        + " from application " + applicationId);

        notificationService.notify(user,
                "Congratulations — you have been hired. Your employee ID is " + saved.getEmployeeCode() + ".",
                "Employee", saved.getId());

        return mapToResponse(saved);
    }


    @Override
    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto, MultipartFile image) {


        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        
        
        
        
        boolean isSelfServiceEmployee = isSelfServiceEmployee();

        if (!isSelfServiceEmployee) {
            employee.setContractNo(dto.getContractNo());
            employee.setJoiningDate(dto.getJoiningDate());

            employee.setStatus(dto.getStatus());
            employee.setEmployeeCode(dto.getEmployeeCode());
            employee.setEmploymentType(dto.getEmploymentType());
        }

        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGender(dto.getGender());
        employee.setBloodGroup(dto.getBloodGroup());

        // Employees may maintain their own salary account details; HR verifies
        // them against a bank document before the next payroll run.
        employee.setBankName(dto.getBankName());
        employee.setBankBranch(dto.getBankBranch());
        employee.setBankAccountName(dto.getBankAccountName());
        employee.setBankAccountNumber(dto.getBankAccountNumber());


        if (image != null && !image.isEmpty()) {
            employee.setImage(uploadImage(image, dto.getFullName()));
        }

        if (!isSelfServiceEmployee && dto.getDepartmentId() != null) {
            employee.setDepartment(
                    departmentRepository.findById(dto.getDepartmentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        }

        if (!isSelfServiceEmployee && dto.getDesignationId() != null) {
            employee.setDesignation(
                    designationRepository.findById(dto.getDesignationId())
                            .orElseThrow(() -> new ResourceNotFoundException("Designation not found")));
        }

        if (!isSelfServiceEmployee && dto.getOfficeId() != null) {
            employee.setOffice(
                    officeRepository.findById(dto.getOfficeId())
                            .orElseThrow(() -> new ResourceNotFoundException("Office not found")));
        }



        if (!isSelfServiceEmployee && dto.getManagerId() != null) {

            if (dto.getManagerId().equals(id)) {
                throw new ValidationException("An employee cannot be their own manager");
            }

            employee.setManager(
                    employeeRepository.findById(dto.getManagerId())
                            .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
        }

        
        
        if (!isSelfServiceEmployee && dto.getPassword() != null && !dto.getPassword().isBlank()) {
            User user = employee.getUser();
            if (user != null) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

        
        
        if (!isSelfServiceEmployee && dto.getRole() != null) {
            User user = employee.getUser();
            if (user != null) {
                user.setRole(dto.getRole());
            }
        }

        if (dto.getPresentAddress() != null) {
            employee.setPresentAddress(
                    updateOrCreateAddress(employee.getPresentAddress(), dto.getPresentAddress()));
        }

        if (dto.getPermanentAddress() != null) {
            employee.setPermanentAddress(
                    updateOrCreateAddress(employee.getPermanentAddress(), dto.getPermanentAddress()));
        }

        Employee updated = employeeRepository.save(employee);

        auditLogService.record("Employee", updated.getId(), AuditAction.UPDATE,
                isSelfServiceEmployee() ? "Self-service profile update" : "Updated by HR/Admin");

        return mapToResponse(updated);
    }

    
    private boolean isSelfServiceEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof User user && user.getRole() == Role.EMPLOYEE;
    }

    
    private Address updateOrCreateAddress(Address existing, AddressRequestDto dto) {
        Address address = existing != null ? existing : new Address();

        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setPostOffice(dto.getPostOffice());
        address.setPostalCode(dto.getPostalCode());

        if (dto.getPoliceStationId() != null) {
            address.setPoliceStation(
                    policeStationRepository.findById(dto.getPoliceStationId())
                            .orElseThrow(() -> new ResourceNotFoundException("Police Station not found")));
        }

        return addressRepository.save(address);
    }



    @Override
    @Transactional
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        employeeRepository.delete(employee);

        auditLogService.record("Employee", id, AuditAction.DELETE,
                "Employee " + employee.getEmployeeCode() + " deleted");

    }



    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {


        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));


        return mapToResponse(employee);

    }




    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {


        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }




    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByCode(String employeeCode) {


        Employee employee =
                employeeRepository.findByEmployeeCode(employeeCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Employee not found"));


        return mapToResponse(employee);

    }




    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByEmail(String email) {


        Employee employee =
                employeeRepository.findByUser_Email(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Employee not found"));


        return mapToResponse(employee);

    }




    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByUserId(Long userId) {


        Employee employee =
                employeeRepository.findByUser_Id(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Employee not found"));


        return mapToResponse(employee);

    }





    private EmployeeResponseDto mapToResponse(Employee employee){


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

        dto.setBankName(employee.getBankName());
        dto.setBankBranch(employee.getBankBranch());
        dto.setBankAccountName(employee.getBankAccountName());
        dto.setBankAccountNumber(employee.getBankAccountNumber());



        

        if(employee.getDepartment()!=null){

            dto.setDepartmentId(
                    employee.getDepartment().getId()
            );

            dto.setDepartmentName(
                    employee.getDepartment().getDepartmentName()
            );
        }



        

        if(employee.getDesignation()!=null){

            dto.setDesignationId(
                    employee.getDesignation().getId()
            );

            dto.setDesignationTitle(
                    employee.getDesignation().getTitle()
            );
        }



        

        if(employee.getOffice()!=null){

            dto.setOfficeId(
                    employee.getOffice().getId()
            );

            dto.setOfficeName(
                    employee.getOffice().getOfficeName()
            );
        }



        

        if(employee.getPresentAddress()!=null){


        }



        

        if (employee.getPermanentAddress() != null) {
            dto.setPermanentAddress(
                    mapAddressResponse(employee.getPermanentAddress())
            );
        }

        if (employee.getPresentAddress() != null) {
            dto.setPresentAddress(
                    mapAddressResponse(employee.getPresentAddress())
            );
        }



        

        if(employee.getUser()!=null){

            dto.setUserId(
                    employee.getUser().getId()
            );


            dto.setFullName(
                    employee.getUser().getFullName()
            );


            dto.setEmail(
                    employee.getUser().getEmail()
            );


            dto.setRole(
                    employee.getUser()
                            .getRole()
                            .name()
            );

        }



        

        if(employee.getManager()!=null){

            dto.setManagerId(
                    employee.getManager().getId()
            );


            if(employee.getManager().getUser()!=null){

                dto.setManagerName(
                        employee.getManager()
                                .getUser()
                                .getFullName()
                );

            }

        }



        return dto;

    }




    



    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByDepartment(Long departmentId) {

        return employeeRepository.findByDepartment_Id(departmentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByDesignation(Long designationId) {

        return employeeRepository.findByDesignation_Id(designationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByOffice(Long officeId) {

        return employeeRepository.findByOffice_Id(officeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByManager(Long managerId) {


        return employeeRepository.findByManager_Id(managerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByStatus(EmployeeStatus status) {


        return employeeRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByEmploymentType(
            EmploymentType employmentType) {


        return employeeRepository
                .findByEmploymentType(employmentType)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByGender(Gender gender) {


        return employeeRepository.findByGender(gender)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> searchEmployees(String keyword) {


        return employeeRepository.searchEmployees(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }







    @Override
    @Transactional(readOnly = true)
    public long getEmployeeCount() {

        return employeeRepository.count();

    }







    @Override
    @Transactional(readOnly = true)
    public long getActiveEmployeeCount() {


        return employeeRepository.countByStatus(
                EmployeeStatus.ACTIVE
        );

    }







    @Override
    @Transactional(readOnly = true)
    public long getInactiveEmployeeCount() {


        return employeeRepository.countByStatus(
                EmployeeStatus.TERMINATED
        );

    }







    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getEmployees(Pageable pageable) {


        return employeeRepository.findAll(pageable)
                .map(this::mapToResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Map<String, Long> getActiveHeadcountByDepartment() {
        java.util.Map<String, Long> result = new java.util.LinkedHashMap<>();
        for (Object[] row : employeeRepository.countActiveEmployeesByDepartment()) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }



    private AddressResponseDto mapAddressResponse(Address address) {

        AddressResponseDto dto = addressMapper.toResponse(address);

        return dto;
    }


    private Address mapAddress(AddressRequestDto dto) {

        if (dto == null) {
            return null;
        }

        Address address = new Address();

        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setPostOffice(dto.getPostOffice());
        address.setPostalCode(dto.getPostalCode());


        if (dto.getPoliceStationId() != null) {
            address.setPoliceStation(
                    policeStationRepository.findById(dto.getPoliceStationId())
                            .orElseThrow(() -> new ResourceNotFoundException("Police Station not found")));
        }

        return address;
    }




    
    private String uploadImage(MultipartFile file, String name) {
        return fileStorageService.store(file, "employee", name);
    }

    
    private String generateEmployeeCode() {
        long next = employeeRepository.count() + 1;
        String candidate;
        do {
            candidate = String.format("EMP-%04d", next++);
        } while (employeeRepository.existsByEmployeeCode(candidate));
        return candidate;
    }

}
