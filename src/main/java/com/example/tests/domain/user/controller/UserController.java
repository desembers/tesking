package com.example.tests.domain.user.controller;

import com.example.tests.domain.common.annotation.Auth;
import com.example.tests.domain.common.dto.AuthUser;
import com.example.tests.domain.user.dto.request.UserChangePasswordRequest;
import com.example.tests.domain.user.dto.response.UserResponse;
import com.example.tests.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@Auth AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }
}
