
package com.day20_application_performance_tuning.dto;

public record BookResponse(
        Long id,
        String title,
        int publicationYear,
        Long authorId,
        String authorName) {
}
