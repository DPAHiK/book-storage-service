package com.example.book_storage_service.controllers;


import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.services.BookService;
import com.example.book_storage_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class MainController {

    @Autowired
    final private UserService userService;

    @Autowired
    final private BookService bookService;

    public MainController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/book")
    public List<Book> getBooks(){

        return bookService.allBooks();
    }

    @GetMapping("/book/{id}")
    public Optional<Book> bookByID(@PathVariable(value = "id") Long id){

        return bookService.bookById(id);
    }

    @GetMapping("/book/isbn/{isbn}")
    public Optional<Book> bookByIsbn(@PathVariable(value = "isbn") String isbn){
        return bookService.bookByIsbn(isbn);
    }

    @PostMapping("/book")
    public void addBook(@RequestBody Book book){
        bookService.addBook(book);
    }

    @PostMapping("/book/{id}")
    public void editBook(@RequestBody Book book, @PathVariable(value = "id") Long id){
        Optional<Book> oldBook = bookService.bookById(id);
        oldBook.ifPresentOrElse(newBook -> {
            newBook.setAuthor(book.getAuthor());
            newBook.setDescription(book.getDescription());
            newBook.setGenre(book.getGenre());
            newBook.setIsbn(book.getIsbn());
            newBook.setTitle(book.getTitle());

            bookService.addBook(newBook);
        },
        () -> {
            System.out.println("While editing: book with id " + id +" have not found");
        });
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable(value = "id") Long id){
        bookService.deleteBookById(id);
    }
}
