package com.example.snsserver.controller;

import com.example.snsserver.dto.follow.response.FollowListResponseDto;
import com.example.snsserver.dto.follow.response.FollowResponseDto;
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

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
@Tag(name = "Follow", description = "팔로우 관련 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "팔로우/언팔로우 토글",
            description = "사용자를 팔로우하거나 언팔로우합니다. 이미 팔로우한 경우 언팔로우로 동작합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우/언팔로우 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (자기 자신 팔로우 시도, 존재하지 않는 사용자 등)"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<FollowResponseDto> toggleFollow(
            @Parameter(description = "팔로우할 사용자의 ID", required = true, example = "2")
            @PathVariable Long followingId
    ) {
        FollowResponseDto response = followService.toggleFollow(followingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/following")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "내 팔로잉 목록 조회",
            description = "현재 사용자가 팔로우하는 사용자 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<List<FollowListResponseDto>> getMyFollowingList() {
        List<FollowListResponseDto> followingList = followService.getMyFollowingList();
        return ResponseEntity.ok(followingList);
    }

    @GetMapping("/my/followers")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "내 팔로워 목록 조회",
            description = "현재 사용자를 팔로우하는 사용자 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<List<FollowListResponseDto>> getMyFollowerList() {
        List<FollowListResponseDto> followerList = followService.getMyFollowerList();
        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/{memberId}/following")
    @Operation(
            summary = "특정 사용자의 팔로잉 목록 조회",
            description = "특정 사용자가 팔로우하는 사용자 목록을 조회합니다. 인증 없이 접근 가능합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자")
    })
    public ResponseEntity<List<FollowListResponseDto>> getFollowingList(
            @Parameter(description = "조회할 사용자의 ID", required = true, example = "1")
            @PathVariable Long memberId
    ) {
        List<FollowListResponseDto> followingList = followService.getFollowingList(memberId);
        return ResponseEntity.ok(followingList);
    }

    @GetMapping("/{memberId}/followers")
    @Operation(
            summary = "특정 사용자의 팔로워 목록 조회",
            description = "특정 사용자를 팔로우하는 사용자 목록을 조회합니다. 인증 없이 접근 가능합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자")
    })
    public ResponseEntity<List<FollowListResponseDto>> getFollowerList(
            @Parameter(description = "조회할 사용자의 ID", required = true, example = "1")
            @PathVariable Long memberId
    ) {
        List<FollowListResponseDto> followerList = followService.getFollowerList(memberId);
        return ResponseEntity.ok(followerList);
    }
}