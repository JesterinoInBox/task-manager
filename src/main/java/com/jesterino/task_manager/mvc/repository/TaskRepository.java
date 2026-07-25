package com.jesterino.task_manager.mvc.repository;

import com.jesterino.task_manager.mvc.entity.Task;
import com.jesterino.task_manager.mvc.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategoryId(Long categoryId);
    List<Task> findByTaskStatus(TaskStatus taskStatus);
    @Query("""
    select t
    from Task t
    where t.user.id = :userId
    """)
    List<Task> findAllByUser(Long userId);
}
