package com.example.tests.domain.manager.repository;

import com.example.tests.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("SELECT m FROM Manager m JOIN FETCH m.user WHERE m.todo.id = :todoId")
    List<org.apache.catalina.Manager> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
