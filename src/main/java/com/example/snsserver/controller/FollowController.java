package com.example.snsserver.controller;

import com.example.snsserver.dto.auth.request.PageRequestDto;
import com.example.snsserver.dto.auth.response.PageResponseDto;
import com.example.snsserver.dto.follow.response.FollowListResponseDto;
import com.example.snsserver.dto.follow.response.FollowResponseDto;
import com.example.snsserver.dto.follow.response.FollowCountResponseDto;
import com.example.snsserver.domain.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
@Tag(name = "Follow", description = "팔로우 관련 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "팔로우/언팔로우 토글", description = "사용자를 팔로우하거나 언팔로우합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우/언팔로우 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<FollowResponseDto> toggleFollow(
            @Parameter(description = "팔로우할 사용자의 ID", required = true, example = "2")
            @PathVariable Long followingId) {
        FollowResponseDto response = followService.toggleFollow(followingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/following")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내 팔로잉 목록 조회", description = "현재 사용자가 팔로우하는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<PageResponseDto<FollowListResponseDto>> getMyFollowingList(
            @Parameter(description = "페이지 요청 정보 (page, size)", required = true)
            @ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(followService.getMyFollowingList(pageRequestDto));
    }

    @GetMapping("/my/followers")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내 팔로워 목록 조회", description = "현재 사용자를 팔로우하는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<PageResponseDto<FollowListResponseDto>> getMyFollowerList(
            @Parameter(description = "페이지 요청 정보 (page, size)", required = true)
            @ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(followService.getMyFollowerList(pageRequestDto));
    }

    @GetMapping("/{username}/following")
    @Operation(summary = "특정 사용자의 팔로잉 목록 조회", description = "특정 사용자가 팔로우하는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자")
    })
    public ResponseEntity<PageResponseDto<FollowListResponseDto>> getFollowingList(
            @Parameter(description = "조회할 사용자의 username", required = true, example = "Test")
            @PathVariable String username,
            @Parameter(description = "페이지 요청 정보 (page, size)", required = true)
            @ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(followService.getFollowingList(username, pageRequestDto));
    }

    @GetMapping("/{username}/followers")
    @Operation(summary = "특정 사용자의 팔로워 목록 조회", description = "특정 사용자를 팔로우하는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자")
    })
    public ResponseEntity<PageResponseDto<FollowListResponseDto>> getFollowerList(
            @Parameter(description = "조회할 사용자의 username", required = true, example = "Test")
            @PathVariable String username,
            @Parameter(description = "페이지 요청 정보 (page, size)", required = true)
            @ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(followService.getFollowerList(username, pageRequestDto));
    }

    @GetMapping("/my/count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내 팔로잉/팔로워 수 조회", description = "현재 사용자의 팔로잉 수와 팔로워 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉/팔로워 수 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<FollowCountResponseDto> getMyFollowCount() {
        FollowCountResponseDto response = followService.getMyFollowCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/count")
    @Operation(summary = "특정 사용자의 팔로잉/팔로워 수 조회", description = "특정 사용자의 팔로잉 수와 팔로워 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉/팔로워 수 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자")
    })
    public ResponseEntity<FollowCountResponseDto> getFollowCount(
            @Parameter(description = "조회할 사용자의 username", required = true, example = "Test")
            @PathVariable String username) {
        FollowCountResponseDto response = followService.getFollowCount(username);
        return ResponseEntity.ok(response);
    }
}