package com.example.book_storage_service.services;

import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.repo.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setIsbn("123-456-789");
        book.setTitle("Test Book");
    }

    @Test
    void testBookByIsbn() {
        when(bookRepository.findByIsbn("123-456-789")).thenReturn(Optional.of(book));
        Optional<Book> foundBook = bookService.bookByIsbn("123-456-789");
        assertTrue(foundBook.isPresent());
        assertEquals("123-456-789", foundBook.get().getIsbn());
        verify(bookRepository, times(1)).findByIsbn("123-456-789");
    }

    @Test
    void testBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> foundBook = bookService.bookById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals(1L, foundBook.get().getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> allBooks = bookService.allBooks();
        assertFalse(allBooks.isEmpty());
        assertEquals(1, allBooks.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(book)).thenReturn(book);
        Book savedBook = bookService.addBook(book);
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        boolean isDeleted = bookService.deleteBookById(1L);
        assertTrue(isDeleted);
        verify(bookRepository, times(1)).deleteById(1L);

    }

}
