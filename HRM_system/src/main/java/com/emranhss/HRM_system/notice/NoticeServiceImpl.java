package com.emranhss.HRM_system.notice;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.office.Office;
import com.emranhss.HRM_system.office.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final OfficeRepository officeRepository;

    @Override

    public NoticeResponseDto saveNotice(NoticeRequestDto noticeRequestDto) {

        Office office = officeRepository.findById(noticeRequestDto.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + noticeRequestDto.getOfficeId()));


        Notice notice = NoticeMapper.toEntity(noticeRequestDto, office);
        Notice savedNotice = noticeRepository.save(notice);

        return NoticeMapper.toResponse(savedNotice);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with id: " + id));
        return NoticeMapper.toResponse(notice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll()
                .stream()
                .map(NoticeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override

    public NoticeResponseDto updateNotice(Long id, NoticeRequestDto noticeRequestDto) {
        Notice existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with id: " + id));

        Office office = officeRepository.findById(noticeRequestDto.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + noticeRequestDto.getOfficeId()));


        NoticeMapper.updateEntity(existingNotice, noticeRequestDto, office);
        Notice updatedNotice = noticeRepository.save(existingNotice);

        return NoticeMapper.toResponse(updatedNotice);
    }

    @Override

    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with id: " + id));
        noticeRepository.delete(notice);
    }

}
