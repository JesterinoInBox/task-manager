package com.jesterino.task_manager.mvc.repository;

import com.jesterino.task_manager.mvc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

}
