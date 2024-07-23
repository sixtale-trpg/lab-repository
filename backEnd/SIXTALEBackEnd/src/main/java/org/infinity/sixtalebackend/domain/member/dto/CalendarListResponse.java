package org.infinity.sixtalebackend.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalendarListResponse {
    private List<CalendarResponse> dateList;
}
