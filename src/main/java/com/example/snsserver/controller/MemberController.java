package com.example.snsserver.controller;

import com.example.snsserver.dto.auth.response.MemberResponseDto;
import com.example.snsserver.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내 정보 조회", description = "인증된 사용자의 정보를 조회합니다.")
    public ResponseEntity<MemberResponseDto> getMyInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @GetMapping("/{username}")
    @Operation(summary = "특정 회원 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getMemberInfo(username));
    }

    @PostMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "프로필 이미지 업로드",
            description = "인증된 사용자의 프로필 이미지를 업로드합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 이미지 업로드 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    public ResponseEntity<MemberResponseDto> updateProfileImage(
            @RequestPart(name = "file", required = true)
            @Schema(description = "업로드할 프로필 이미지", type = "string", format = "binary")
            MultipartFile file) {
        return ResponseEntity.ok(memberService.updateProfileImage(file));
    }
}