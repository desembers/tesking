package com.example.tests.domain.user.service;

import com.example.tests.domain.common.exception.InValidRequestException;
import com.example.tests.domain.user.dto.request.UserRoleChangeRequest;
import com.example.tests.domain.user.entity.User;
import com.example.tests.domain.user.enums.UserRole;
import com.example.tests.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAdminService {
    private final UserRepository userRepository;

    @Transactional
    public void changeUserRole(long userId, UserRoleChangeRequest userRoleChangeRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InValidRequestException("User not found"));
        user.updateRole(UserRole.of(userRoleChangeRequest.getRole()));
    }
}
