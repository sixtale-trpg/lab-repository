package org.infinity.sixtalebackend.domain.member.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    @NonNull
    private String nickName;
    private String imageURL;

}
