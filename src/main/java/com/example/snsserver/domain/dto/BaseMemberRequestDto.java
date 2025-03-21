package com.example.snsserver.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseMemberRequestDto {
    @Size(min = 1, max = 10, message = "아이디는 10자리 이상 입력 불가합니다")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;
}
