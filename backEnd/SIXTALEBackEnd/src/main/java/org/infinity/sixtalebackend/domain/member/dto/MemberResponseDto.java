package org.infinity.sixtalebackend.domain.member.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {

    @NonNull
    private String nickName;
    private String imageURL;

}
