
package com.day20_application_performance_tuning.controller;

import com.day20_application_performance_tuning.dto.BookResponse;
import com.day20_application_performance_tuning.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> findByPublicationYear(
            @RequestParam int publicationYear) {
        return bookService.findByPublicationYear(publicationYear);
    }
}
