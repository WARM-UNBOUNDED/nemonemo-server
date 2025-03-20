package com.example.snsserver.domain.controller;


import com.example.snsserver.domain.domain.Member;
import com.example.snsserver.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Member> getMyInfo() {
        Member member = memberService.getMyInfo();
        return ResponseEntity.ok(member);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Member> getMemberInfo(@PathVariable String username) {
        Member member = memberService.getMemberInfo(username);
        return ResponseEntity.ok(member);
    }
}