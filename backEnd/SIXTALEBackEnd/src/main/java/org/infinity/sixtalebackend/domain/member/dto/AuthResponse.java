package org.infinity.sixtalebackend.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    String id;
    String email;
    String nickname;
}