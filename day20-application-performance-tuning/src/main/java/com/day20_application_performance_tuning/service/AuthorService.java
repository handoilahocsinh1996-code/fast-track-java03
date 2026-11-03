
package com.day20_application_performance_tuning.service;

import com.day20_application_performance_tuning.dto.AuthorBooksResponse;
import com.day20_application_performance_tuning.model.Author;
import com.day20_application_performance_tuning.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Baseline implementation to demonstrate N+1 Query.
     */
    @Transactional(readOnly = true)
    public List<AuthorBooksResponse> getAuthorsWithBooksNPlusOne() {

        List<Author> authors = authorRepository.findAll();

        return authors.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Optimized implementation using EntityGraph.
     */
    @Transactional(readOnly = true)
    public List<AuthorBooksResponse> getAuthorsWithBooksOptimized() {

        List<Author> authors = authorRepository.findAllWithBooks();

        return authors.stream()
                .map(this::toResponse)
                .toList();
    }

    private AuthorBooksResponse toResponse(Author author) {

        List<String> titles = author.getBooks().stream()
                .map(book -> book.getTitle())
                .toList();

        return new AuthorBooksResponse(
                author.getId(),
                author.getName(),
                titles.size(),
                titles);
    }
}
