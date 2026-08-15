package com.emranhss.HRM_system.applicant;

import com.emranhss.HRM_system.exception.ConflictException;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;

import com.emranhss.HRM_system.application.*;
import com.emranhss.HRM_system.enums.Role;
import com.emranhss.HRM_system.jobpost.JobPost;
import com.emranhss.HRM_system.jobpost.JobPostRepository;
import com.emranhss.HRM_system.user.User;
import com.emranhss.HRM_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    
    @Override
    public ApplicantResponseDto create(ApplicantRequestDto dto) {

        User caller = getCurrentUser();
        if (caller == null) {
            throw new ValidationException("You must be logged in to create an applicant profile.");
        }

        if (caller.getRole() == Role.ADMIN || caller.getRole() == Role.HR) {
            return createOnBehalfOfCandidate(dto);
        }

        if (applicantRepository.findByEmail(caller.getEmail()).isPresent()) {
            throw new ConflictException("An applicant profile already exists for this account.");
        }

        Applicant applicant = ApplicantMapper.toEntity(dto);
        applicant.setEmail(caller.getEmail());
        applicant.setUser(caller);

        return ApplicantMapper.toDTO(
                applicantRepository.save(applicant)
        );

    }

    private ApplicantResponseDto createOnBehalfOfCandidate(ApplicantRequestDto dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new ValidationException("Email is required.");
        }

        if (userRepository.existsByEmail(dto.getEmail()) || applicantRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("An account already exists with this email.");
        }

        String rawPassword = (dto.getPassword() != null && !dto.getPassword().isBlank())
                ? dto.getPassword()
                : java.util.UUID.randomUUID().toString();

        User candidateUser = new User();
        candidateUser.setFullName(dto.getName());
        candidateUser.setEmail(dto.getEmail());
        candidateUser.setPassword(passwordEncoder.encode(rawPassword));
        candidateUser.setRole(Role.APPLICANT);
        candidateUser.setEnabled(true);
        userRepository.save(candidateUser);

        Applicant applicant = ApplicantMapper.toEntity(dto);
        applicant.setUser(candidateUser);

        return ApplicantMapper.toDTO(
                applicantRepository.save(applicant)
        );

    }


    @Override
    public ApplicantResponseDto getById(Long id) {


        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Applicant not found")
                );


        return ApplicantMapper.toDTO(applicant);

    }


    @Override
    public List<ApplicantResponseDto> getAll() {


        return applicantRepository.findAll()
                .stream()
                .map(ApplicantMapper::toDTO)
                .collect(Collectors.toList());

    }


    @Override
    public ApplicantResponseDto update(Long id, ApplicantRequestDto dto) {


        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Applicant not found")
                );


        applicant.setName(dto.getName());

        applicant.setEmail(dto.getEmail());

        applicant.setPhone(dto.getPhone());

        applicant.setAddress(dto.getAddress());


        
        applicant.setEducation(dto.getEducation());

        applicant.setExperience(dto.getExperience());


        applicant.setSkills(dto.getSkills());

        applicant.setCvPath(dto.getCvPath());


        
        
        
        
        
        
        User user = applicant.getUser();


        if (user != null && user.getRole() == Role.APPLICANT) {

            user.setFullName(dto.getName());

            user.setEmail(dto.getEmail());


            if (dto.getPassword() != null
                    && !dto.getPassword().isEmpty()) {

                user.setPassword(
                        passwordEncoder.encode(dto.getPassword())
                );

            }


            userRepository.save(user);
        }


        return ApplicantMapper.toDTO(
                applicantRepository.save(applicant)
        );

    }


    @Override
    public void delete(Long id) {


        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Applicant not found")
                );


        
        
        
        
        if (applicant.getUser() != null && applicant.getUser().getRole() == Role.APPLICANT) {

            userRepository.delete(applicant.getUser());

        }


        applicantRepository.delete(applicant);

    }


    @Override
    public ApplicantResponseDto getByEmail(String email) {


        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Applicant not found")
                );


        return ApplicantMapper.toDTO(applicant);

    }

    @Override
    public List<ApplicantResponseDto> searchApplicants(String keyword) {
        return applicantRepository.searchApplicants(keyword)
                .stream()
                .map(ApplicantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getApplicantCount() {
        return applicantRepository.count();
    }

    @Override
    public Page<ApplicantResponseDto> getApplicants(Pageable pageable) {
        return applicantRepository.findAll(pageable)
                .map(ApplicantMapper::toDTO);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

}
