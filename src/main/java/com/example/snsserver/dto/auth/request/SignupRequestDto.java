package com.example.snsserver.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto extends BaseMemberRequestDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
}