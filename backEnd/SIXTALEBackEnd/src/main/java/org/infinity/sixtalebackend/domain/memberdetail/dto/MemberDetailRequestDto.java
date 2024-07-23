package org.infinity.sixtalebackend.domain.memberdetail.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberDetailRequestDto {

    private String favorRule;

    @NotNull
    private String rpType;
    @NotNull
    private String chatType;
    @NotNull
    private String talkType;
    @NotNull
    private String tasteType;
    @NotNull
    private String systemType;
    @NotNull
    private String timeType;
    @NotNull
    @NotEmpty
    private List<Long> genreList;
}
