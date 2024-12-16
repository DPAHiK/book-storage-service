package com.example.book_storage_service.controllers;


import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.services.BookService;
import com.example.book_storage_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @PostMapping("/book")
    public void addBook(@RequestBody Book book){
        bookService.addBook(book);
    }
}
