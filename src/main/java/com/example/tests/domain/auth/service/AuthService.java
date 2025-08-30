package com.example.tests.domain.auth.service;

import com.example.tests.config.JwtUtil;
import com.example.tests.config.PasswordEncoder;
import com.example.tests.domain.auth.dto.request.SigninRequest;
import com.example.tests.domain.auth.dto.request.SignupRequest;
import com.example.tests.domain.auth.dto.response.SigninResponse;
import com.example.tests.domain.auth.dto.response.SignupResponse;
import com.example.tests.domain.auth.exception.AuthException;
import com.example.tests.domain.common.exception.InValidRequestException;
import com.example.tests.domain.user.entity.User;
import com.example.tests.domain.user.enums.UserRole;
import com.example.tests.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new InValidRequestException("이미 존재하는 이메일입니다.");
        }

        User newUser = new User(signupRequest.getEmail(), encodedPassword, userRole);
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new SignupResponse(bearerToken);
    }

    @Transactional
    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> new InValidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponse(bearerToken);
    }
}
