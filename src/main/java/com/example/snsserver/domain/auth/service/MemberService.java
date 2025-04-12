package com.example.snsserver.domain.auth.service;

import com.example.snsserver.common.service.BaseImageService;
import com.example.snsserver.domain.auth.repository.MemberRepository;
import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.dto.auth.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BaseImageService baseImageService;

    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = getMemberByUsername(username);
        return new MemberResponseDto(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String username) {
        Member member = getMemberByUsername(username);
        return new MemberResponseDto(member);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "members", key = "#username")
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));
    }

    @Transactional
    public MemberResponseDto updateProfileImage(MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = getMemberByUsername(username);

        baseImageService.deleteImage(member.getProfileImagePath());

        String profileImagePath = baseImageService.uploadImage(file, "profile");
        member.updateProfileImage(profileImagePath);

        memberRepository.save(member);
        return new MemberResponseDto(member);
    }
}