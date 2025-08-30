package com.example.tests.domain.comment.service;

import com.example.tests.domain.comment.dto.request.CommentSaveRequest;
import com.example.tests.domain.comment.dto.response.CommentResponse;
import com.example.tests.domain.comment.dto.response.CommentSaveResponse;
import com.example.tests.domain.comment.entity.Comment;
import com.example.tests.domain.comment.repository.CommentRepository;
import com.example.tests.domain.common.dto.AuthUser;
import com.example.tests.domain.common.exception.InValidRequestException;
import com.example.tests.domain.todo.entity.Todo;
import com.example.tests.domain.todo.repository.TodoRepository;
import com.example.tests.domain.user.dto.response.UserResponse;
import com.example.tests.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentSaveResponse saveComment(AuthUser authUser, long todoId, CommentSaveRequest commentSaveRequest) {
        User user = User.fromAuthUser(authUser);
        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
                new InValidRequestException("Todo not found"));

        Comment newComment = new Comment(
                commentSaveRequest.getContents(),
                user,
                todo
        );

        Comment savedComment = commentRepository.save(newComment);

        return new CommentSaveResponse(
                savedComment.getId(),
                savedComment.getContents(),
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    @Transactional
    public List<CommentResponse> getComments(long todoId) {
        List<Comment> commentList = commentRepository.findByTodoIdWithUser(todoId);

        List<CommentResponse> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            User user = comment.getUser();
            CommentResponse dto = new CommentResponse(
                    comment.getId(),
                    comment.getContents(),
                    new UserResponse(user.getId(), user.getEmail())
            );
            dtoList.add(dto);
        }
        return dtoList;
    }
}
