package org.infinity.sixtalebackend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.dto.MemberResponseDto;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.infinity.sixtalebackend.infra.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSerivceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public boolean isNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    /**
     * 회원 정보 생성
     * @param id
     * @param nickName
     * @param files
     */
    @Override
    public MemberResponseDto createMember(Long id, String nickName, MultipartFile[] files) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member with id " + id + " not found"));

        member.setNickName(nickName); // 닉네임만 수정

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .nickName(nickName)
                .build();

        // 프로필 이미지 저장
        if (files != null && files.length > 0) {
            List<String> listUrl = s3Service.upload(files, id + "/" + "profile");
            member.setImageURL(listUrl.get(0));
            memberResponseDto.setImageURL(listUrl.get(0));
        }

        memberRepository.save(member); // 변경 사항 저장

        return memberResponseDto;
    }
}
