package com.example.tests.AOP;

import com.example.tests.domain.auth.service.AuthService;
import com.example.tests.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.management.relation.Role;

import static org.awaitility.Awaitility.given;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 테스트용 설정이 있다면
public class AdminApiLoggingAspectTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        User admin = new User(1L, "admin@example.com", Role.ADMIN);
        given(authService.getCurrentUser()).willReturn(admin);
    }

    @Test
    void deleteComment_호출시_AOP_로깅이_정상작동한다() throws Exception {
        mockMvc.perform(delete("/admin/comment/123")
                        .sessionAttr("LOGIN_USER", new User(1L, "admin@example.com", Role.ADMIN)))
                .andExpect(status().isOk()); // 또는 NoContent 등

        // 로깅 자체는 콘솔로 출력되므로 여기서는 AOP 오류 없이 통과만 확인
    }

    @Test
    void changeUserRole_호출시_AOP_로깅이_정상작동한다() throws Exception {
        String json = """
            {
              "userId": 5,
              "newRole": "MANAGER"
            }
            """;

        mockMvc.perform(put("/admin/user/change-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .sessionAttr("LOGIN_USER", new User(1L, "admin@example.com", Role.ADMIN)))
                .andExpect(status().isOk());
    }

}
