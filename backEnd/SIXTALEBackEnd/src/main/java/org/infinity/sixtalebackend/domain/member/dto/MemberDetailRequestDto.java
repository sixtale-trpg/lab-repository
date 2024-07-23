package org.infinity.sixtalebackend.domain.member.dto;

import jakarta.persistence.Column;
import org.antlr.v4.runtime.misc.NotNull;

public class MemberDetailRequestDto {
    private String favorRule;
    private Integer rpType;
    private Integer chatType;
    private Integer talkType;
    private Integer tasteType;
    private Integer systemType;
    private Integer timeType;
}
