package com.example.tests.domain.comment.dto.response;

import com.example.tests.domain.user.dto.response.UserResponse;
import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final String contents;
    private final UserResponse user;

    public CommentResponse(Long id, String contents, UserResponse user) {
        this.id = id;
        this.contents = contents;
        this.user = user;
    }
}
