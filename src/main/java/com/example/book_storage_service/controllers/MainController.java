package com.example.book_storage_service.controllers;


import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.services.BookService;
import com.example.book_storage_service.services.ProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class MainController {


    final private ProducerService producerService;

    final private BookService bookService;

    public MainController(ProducerService producerService, BookService bookService) {
        this.producerService = producerService;
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
    public ResponseEntity<?> addBook(@RequestBody Book book){
        bookService.addBook(book);

        producerService.sendBookId("add-book-topic", book.getId().toString());

        return ResponseEntity.ok("Book added");
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<?> editBook(@RequestBody Book book, @PathVariable(value = "id") Long id){
        Optional<Book> oldBook = bookService.bookById(id);
        if(oldBook.isPresent()){
            Book newBook = oldBook.get();

            newBook.setAuthor(book.getAuthor());
            newBook.setDescription(book.getDescription());
            newBook.setGenre(book.getGenre());
            newBook.setIsbn(book.getIsbn());
            newBook.setTitle(book.getTitle());

            bookService.addBook(newBook);

            return ResponseEntity.ok("Book edited");
        }
            System.out.println("While editing: book with id " + id +" not found");


        return ResponseEntity.status(404).body("Book with id " + id +" not found");
    }

    @DeleteMapping("/book/{id}")
    public boolean deleteBook(@PathVariable(value = "id") Long id){

        boolean result = bookService.deleteBookById(id);

        if(result) producerService.sendBookId("delete-book-topic", id.toString());

        return  result;
    }
}
