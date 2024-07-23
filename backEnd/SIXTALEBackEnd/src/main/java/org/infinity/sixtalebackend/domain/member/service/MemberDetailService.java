package org.infinity.sixtalebackend.domain.member.service;

import org.infinity.sixtalebackend.domain.member.dto.MemberDetailRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberDetailService {
    void createMemberDetail(MemberDetailRequestDto memberDetailRequestDto, Long memberID);

    void updateMemberDetail(MemberDetailRequestDto memberDetailRequestDto, Long memberID);
}
