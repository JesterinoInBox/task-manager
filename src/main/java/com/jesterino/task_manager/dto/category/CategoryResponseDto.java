package com.jesterino.task_manager.dto.category;

import java.io.Serializable;

public record CategoryResponseDto(
        Long id,
        String categoryName
) implements Serializable {
}
