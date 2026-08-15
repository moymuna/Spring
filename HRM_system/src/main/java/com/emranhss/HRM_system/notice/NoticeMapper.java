package com.emranhss.HRM_system.notice;

import com.emranhss.HRM_system.office.Office;

public class NoticeMapper {
public static Notice toEntity(NoticeRequestDto dto,
                              Office office) {

    Notice notice = new Notice();

    
    notice.setTitle(dto.getTitle());
    notice.setDescription(dto.getDescription());
    notice.setPublishDate(dto.getPublishDate());

    
    notice.setOffice(office);

    return notice;
}

    
    public static NoticeResponseDto toResponse(Notice notice) {

        NoticeResponseDto dto = new NoticeResponseDto();

        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setDescription(notice.getDescription());
        dto.setPublishDate(notice.getPublishDate());

        if (notice.getOffice() != null) {

            dto.setOfficeId(notice.getOffice().getId());
            dto.setOfficeName(notice.getOffice().getOfficeName());
        }

        return dto;
    }

    
    public static void updateEntity(Notice notice,
                                    NoticeRequestDto dto,
                                    Office office) {

        notice.setTitle(dto.getTitle());
        notice.setDescription(dto.getDescription());
        notice.setPublishDate(dto.getPublishDate());

        notice.setOffice(office);
    }

}
