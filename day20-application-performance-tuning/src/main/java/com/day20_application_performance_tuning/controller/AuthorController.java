
package com.day20_application_performance_tuning.controller;

import com.day20_application_performance_tuning.dto.AuthorBooksResponse;
import com.day20_application_performance_tuning.service.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Endpoint demonstrating N+1 Query.
    @GetMapping("/with-books/n-plus-one")
    public List<AuthorBooksResponse> getWithBooksNPlusOne() {
        return authorService.getAuthorsWithBooksNPlusOne();
    }

    // Optimized endpoint using EntityGraph.
    @GetMapping("/with-books/optimized")
    public List<AuthorBooksResponse> getWithBooksOptimized() {
        return authorService.getAuthorsWithBooksOptimized();
    }
}
