
package com.day20_application_performance_tuning.repository;

import com.day20_application_performance_tuning.model.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Optimized query: fetch Author and Books together.
    @EntityGraph(attributePaths = "books")
    @Query("SELECT DISTINCT a FROM Author a")
    List<Author> findAllWithBooks();
}
