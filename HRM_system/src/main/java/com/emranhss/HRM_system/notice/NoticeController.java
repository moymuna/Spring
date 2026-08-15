package com.emranhss.HRM_system.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto noticeRequestDto) {
        NoticeResponseDto createdNotice = noticeService.saveNotice(noticeRequestDto);
        return new ResponseEntity<>(createdNotice, HttpStatus.CREATED);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<NoticeResponseDto> getNoticeById(@PathVariable Long id) {
        NoticeResponseDto notice = noticeService.getNoticeById(id);
        return ResponseEntity.ok(notice);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        List<NoticeResponseDto> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeRequestDto noticeRequestDto) {
        NoticeResponseDto updatedNotice = noticeService.updateNotice(id, noticeRequestDto);
        return ResponseEntity.ok(updatedNotice);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> deleteNotice(
            @PathVariable Long id) {
        noticeService.deleteNotice(id);



        return ResponseEntity.ok(" Notice Deleted Successfully"
        );
    }

}
