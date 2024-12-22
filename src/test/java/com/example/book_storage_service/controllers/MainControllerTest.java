package com.example.book_storage_service.controllers;

import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.services.BookService;
import com.example.book_storage_service.services.ProducerService;
import com.example.book_storage_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ProducerService producerService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private MainController mainController;

    private MockMvc mockMvc;
    private Book book;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

        book = new Book();
        book.setId(1L);
        book.setIsbn("123-456-789");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
    }

    @Test
    void testGetBooks() throws Exception {
        when(bookService.allBooks()).thenReturn(Arrays.asList(book));

        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Book")));

        verify(bookService, times(1)).allBooks();
    }

    @Test
    void testBookByID() throws Exception {
        when(bookService.bookById(anyLong())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Book")));

        verify(bookService, times(1)).bookById(1L);
    }

    @Test
    void testBookByIsbn() throws Exception {
        when(bookService.bookByIsbn(anyString())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/isbn/{isbn}", "123-456-789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is("123-456-789")));

        verify(bookService, times(1)).bookByIsbn("123-456-789");
    }

    @Test
    void testAddBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"isbn\":\"123-456-789\",\"title\":\"Test Book\",\"author\":\"Test Author\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added"));

        verify(bookService, times(1)).addBook(any(Book.class));
        verify(producerService, times(1)).sendBookId(eq("add-book-topic"), anyString());
    }

    @Test
    void testEditBook() throws Exception {
        when(bookService.bookById(anyLong())).thenReturn(Optional.of(book));

        mockMvc.perform(put("/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"isbn\":\"123-456-789\",\"title\":\"Edited Book\",\"author\":\"Edited Author\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book edited"));

        verify(bookService, times(1)).bookById(1L);
        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        when(bookService.deleteBookById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/book/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(bookService, times(1)).deleteBookById(1L);
        verify(producerService, times(1)).sendBookId(eq("delete-book-topic"), anyString());
    }
}
