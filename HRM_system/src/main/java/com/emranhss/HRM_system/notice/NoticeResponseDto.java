package com.emranhss.HRM_system.notice;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date publishDate;
    private Long officeId;
    private String officeName;
}
