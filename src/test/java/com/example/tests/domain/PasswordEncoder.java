package com.example.tests.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class PasswordEncoder {
    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드가_정상적으로_동작한다1() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        boolean matches = passwordEncoder.matches(encodedPassword, rawPassword);

        // then
        assertTrue(matches);
    }

    @Test
    void matches_메서드가_정상적으로_동작한다2() {
        String raw = "testPassword1!";
        String encoded = passwordEncoder.encode(raw);
        assertTrue(passwordEncoder.matches(raw, encoded));
    }

}
