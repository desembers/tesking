package com.example.tests.domain.user.enums;

import com.example.tests.domain.common.exception.InValidRequestException;

import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InValidRequestException("유효하지 않은 UerRole"));
    }

}
