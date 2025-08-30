package com.example.tests.AOP;

import com.example.tests.domain.auth.service.AuthService;
import com.example.tests.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.management.relation.Role;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AdminApiLogginAspectpyTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @SpyBean
    private AdminApiLoggingAspect loggingAspect;

    @Test
    void AOP가_정상적으로_적용된다() throws Exception {
        User admin = new User(1L, "admin@example.com", Role.ADMIN);
        given(authService.getCurrentUser()).willReturn(admin);

        mockMvc.perform(delete("/admin/comment/123")
                        .sessionAttr("LOGIN_USER", admin))
                .andExpect(status().isOk());

        // AOP 내부 메서드 호출 여부 확인
        verify(loggingAspect, atLeastOnce()).logAdminApi(any());
    }
}
