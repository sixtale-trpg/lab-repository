package org.infinity.sixtalebackend.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
public class CalendarRequest {
    @NotBlank
    private LocalDateTime startAt;

    @NotBlank
    private LocalDateTime endAt;

    private String title;

}
