package org.infinity.sixtalebackend.domain.member.service;

import org.infinity.sixtalebackend.domain.member.dto.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    public boolean isNicknameDuplicated(String nickname);
    MemberResponseDto createMember(Long id, String nickName, MultipartFile[] files) throws IOException;
}
