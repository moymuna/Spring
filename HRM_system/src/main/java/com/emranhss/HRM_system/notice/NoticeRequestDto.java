package com.emranhss.HRM_system.notice;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeRequestDto {
    private String title;

    private String description;

    private Date publishDate;

    private Long officeId;
}
