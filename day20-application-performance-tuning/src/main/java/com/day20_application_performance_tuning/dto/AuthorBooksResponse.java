
package com.day20_application_performance_tuning.dto;

import java.util.List;

public record AuthorBooksResponse(
        Long authorId,
        String authorName,
        int bookCount,
        List<String> bookTitles) {
}
