package com.example.snsserver.domain.dto;

import com.example.snsserver.domain.domain.Authority;
import com.example.snsserver.domain.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String name;
    private String username;
    private Authority authority;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.username = member.getUsername();
        this.authority = member.getAuthority();
    }
}

