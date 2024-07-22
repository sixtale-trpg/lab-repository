package org.infinity.sixtalebackend.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CalendarRequest {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;

}
