
package com.day20_application_performance_tuning.config;

import com.day20_application_performance_tuning.model.Author;
import com.day20_application_performance_tuning.model.Book;
import com.day20_application_performance_tuning.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(AuthorRepository authorRepository) {

        return args -> {

            // Avoid inserting duplicate sample data.
            if (authorRepository.count() > 0) {
                return;
            }

            for (int authorNumber = 1; authorNumber <= 30; authorNumber++) {

                Author author = new Author();
                author.setName("Author " + authorNumber);

                for (int bookNumber = 1; bookNumber <= 100; bookNumber++) {

                    int year = 1990 + ((authorNumber * 7 + bookNumber) % 35);

                    Book book = new Book(
                            "Book " + bookNumber + " - Author " + authorNumber,
                            year);

                    author.addBook(book);
                }

                authorRepository.save(author);
            }
        };
    }
}
