package com.example.snsserver.domain.dto;

import com.example.snsserver.domain.domain.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto extends BaseMemberRequestDto {
    private Authority authority;
}

