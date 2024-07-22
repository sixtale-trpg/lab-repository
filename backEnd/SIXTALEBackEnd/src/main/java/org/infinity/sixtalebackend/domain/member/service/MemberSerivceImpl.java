package org.infinity.sixtalebackend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSerivceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public boolean isNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
