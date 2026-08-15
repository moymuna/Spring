package com.emranhss.HRM_system.office;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OfficeService {

    OfficeResponseDto create(OfficeRequestDto dto);


    OfficeResponseDto getById(Long id);


    List<OfficeResponseDto> getAll();


    OfficeResponseDto update(Long id, OfficeRequestDto dto);


    void delete(Long id);

    List<OfficeResponseDto> search(String keyword);

    long getOfficeCount();

    Page<OfficeResponseDto> getOffices(Pageable pageable);

}
