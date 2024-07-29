package org.infinity.sixtalebackend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.dto.MemberResponseDto;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.infinity.sixtalebackend.infra.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public MemberResponseDto createMemberInfo(Long id, String nickName, MultipartFile[] files) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + id));

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

    /**
     * 회원 정보 수정
     * @param id
     * @param nickName
     * @param files
     * @return
     * @throws IOException
     */
    @Override
    public MemberResponseDto updateMemberInfo(Long id, String nickName, MultipartFile[] files) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + id));

        // 응답 DTO 생성
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .nickName(member.getNickname())
                .imageURL(member.getImageURL())
                .build();

        // 닉네임 수정
        if(nickName!=null && !nickName.trim().isEmpty()) {
            member.setNickName(nickName);
            memberResponseDto.setNickName(nickName);
        }

        // 기존 프로필 이미지 삭제
        if (member.getImageURL() != null) {
            String oldImageName = member.getImageURL().substring(member.getImageURL().lastIndexOf("/")+1);
            System.out.println(oldImageName);
            s3Service.delete(id + "/"+"profile/" + oldImageName);
        }

        // 프로필 이미지 저장
        if (files != null && files.length > 0) {
            List<String> listUrl = s3Service.upload(files, id + "/" + "profile");
            member.setImageURL(listUrl.get(0));
            memberResponseDto.setImageURL(listUrl.get(0));
        }

        memberRepository.save(member); // 변경 사항 저장

        return memberResponseDto;
    }


    /**
     * 회원 정보 조회
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public MemberResponseDto getMemberInfo(Long id) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + id));

        return MemberResponseDto.builder()
                .nickName(member.getNickname())
                .imageURL(member.getImageURL())
                .build();
    }

}