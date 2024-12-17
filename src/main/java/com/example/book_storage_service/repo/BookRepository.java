package com.example.book_storage_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.book_storage_service.models.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>{
    Optional<Book> findByIsbn(String isbn);
}
