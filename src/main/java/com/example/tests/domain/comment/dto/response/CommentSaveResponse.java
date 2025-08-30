package com.example.tests.domain.comment.dto.response;

import com.example.tests.domain.user.dto.response.UserResponse;
import lombok.Getter;

@Getter
public class CommentSaveResponse {
    private final Long id;
    private final String contents;
    private final UserResponse user;

    public CommentSaveResponse(Long id, String contents, UserResponse user) {
        this.id = id;
        this.contents = contents;
        this.user = user;
    }
}
