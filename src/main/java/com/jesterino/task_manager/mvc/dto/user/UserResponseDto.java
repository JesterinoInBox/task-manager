package com.jesterino.task_manager.mvc.dto.user;

import java.io.Serializable;

public record UserResponseDto(
        Long id,
        String name
) implements Serializable {}
