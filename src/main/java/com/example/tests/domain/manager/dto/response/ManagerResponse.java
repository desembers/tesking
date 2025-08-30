package com.example.tests.domain.manager.dto.response;

import com.example.tests.domain.user.dto.response.UserResponse;
import lombok.Getter;

@Getter
public class ManagerResponse {
    private final Long id;
    private final UserResponse user;

    public ManagerResponse(Long id, UserResponse user) {
        this.id = id;
        this.user = user;
    }
}
