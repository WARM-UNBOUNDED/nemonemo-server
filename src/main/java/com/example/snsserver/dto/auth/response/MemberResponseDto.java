package com.example.snsserver.dto.auth.response;

import com.example.snsserver.domain.auth.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String username;
    private String authority;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.authority = member.getAuthority() != null ? member.getAuthority().name() : null;
    }
}
