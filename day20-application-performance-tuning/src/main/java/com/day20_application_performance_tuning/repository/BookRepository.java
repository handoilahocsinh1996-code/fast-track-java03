package com.day20_application_performance_tuning.repository;

import com.day20_application_performance_tuning.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByPublicationYear(int publicationYear);
}