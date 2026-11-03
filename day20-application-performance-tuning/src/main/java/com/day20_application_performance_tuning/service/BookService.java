
package com.day20_application_performance_tuning.service;

import com.day20_application_performance_tuning.dto.BookResponse;
import com.day20_application_performance_tuning.model.Book;
import com.day20_application_performance_tuning.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByPublicationYear(int publicationYear) {

        return bookRepository.findByPublicationYear(publicationYear)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BookResponse toResponse(Book book) {

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getAuthor().getId(),
                book.getAuthor().getName());
    }
}
