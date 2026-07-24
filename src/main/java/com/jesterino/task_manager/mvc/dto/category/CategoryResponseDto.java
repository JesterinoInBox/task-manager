package com.jesterino.task_manager.mvc.dto.category;

import java.io.Serializable;

public record CategoryResponseDto(
        Long id,
        String categoryName
) implements Serializable {
}
