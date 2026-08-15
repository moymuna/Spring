package com.emranhss.HRM_system.notice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {
    NoticeResponseDto saveNotice(NoticeRequestDto noticeRequestDto);

    NoticeResponseDto getNoticeById(Long id);

    List<NoticeResponseDto> getAllNotices();

    NoticeResponseDto updateNotice(Long id, NoticeRequestDto noticeRequestDto);

    void deleteNotice(Long id);
}
