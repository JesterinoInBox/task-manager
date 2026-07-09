package com.jesterino.task_manager.dto.user;

import java.io.Serializable;

public record UserResponseDto(
        Long id,
        String name
) implements Serializable {}
