package com.emranhss.HRM_system.location.district;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.location.division.Division;
import com.emranhss.HRM_system.location.division.DivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    
    @Override
    @Transactional
    public DistrictResponseDto createDistrict(DistrictRequestDto requestDto) {

        Division division = divisionRepository.findById(requestDto.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found with id: " + requestDto.getDivisionId()));

        District district = DistrictMapper.toEntity(requestDto, division);

        District saved = districtRepository.save(district);

        return DistrictMapper.toResponse(saved);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponseDto> getAllDistricts() {
        return districtRepository.findAll()
                .stream()
                .map(DistrictMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public DistrictResponseDto getDistrictById(Long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + id));

        return DistrictMapper.toResponse(district);
    }

    
    @Override
    @Transactional
    public DistrictResponseDto updateDistrict(Long id, DistrictRequestDto requestDto) {

        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + id));

        Division division = divisionRepository.findById(requestDto.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found with id: " + requestDto.getDivisionId()));

        DistrictMapper.updateEntity(district, requestDto, division);

        District updated = districtRepository.save(district);

        return DistrictMapper.toResponse(updated);
    }

    
    @Override
    @Transactional
    public void deleteDistrict(Long id) {

        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + id));

        districtRepository.delete(district);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponseDto> getDistrictsByDivisionId(Long divisionId) {
        return districtRepository.findByDivisionId(divisionId)
                .stream()
                .map(DistrictMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponseDto> getDistrictsByDivisionName(String divisionName) {
        return districtRepository.findByDivisionName(divisionName)
                .stream()
                .map(DistrictMapper::toResponse)
                .collect(Collectors.toList());
    }
}
