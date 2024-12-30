package com.example.book_storage_service.controllers;


import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.response_and_request.ResponseHandler;
import com.example.book_storage_service.services.BookService;
import com.example.book_storage_service.services.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    final private ProducerService producerService;

    final private BookService bookService;

    public BookController(ProducerService producerService, BookService bookService) {
        this.producerService = producerService;
        this.bookService = bookService;
    }

    @GetMapping("/book")
    public ResponseEntity<?> getBooks(){

        return ResponseHandler.generateResponse(HttpStatus.OK, "data", bookService.allBooks());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> bookByID(@PathVariable(value = "id") Long id){

        Optional<Book> book = bookService.bookById(id);

        if(book.isPresent()) return ResponseHandler.generateResponse(HttpStatus.OK, "data", book);

        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "message", "Book with id " + id + " not found");

    }

    @GetMapping("/book/isbn/{isbn}")
    public ResponseEntity<?> bookByIsbn(@PathVariable(value = "isbn") String isbn){

        Optional<Book> book = bookService.bookByIsbn(isbn);

        if(book.isPresent()) return ResponseHandler.generateResponse(HttpStatus.OK, "data", book);

        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "message", "Book with ISBN " + isbn + " not found");
    }

    @PostMapping("/book")
    public ResponseEntity<?> addBook(@RequestBody Book book){
        if(bookService.bookByIsbn(book.getIsbn()).isPresent()) return ResponseHandler.generateResponse(HttpStatus.CONFLICT, "message", "Book with ISBN " + book.getIsbn() + " already exists");

        bookService.addBook(book);

        producerService.sendBookId("add-book-topic", book.getId().toString());

        return ResponseHandler.generateResponse(HttpStatus.OK, "message", "Book added");
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<?> editBook(@RequestBody Book book, @PathVariable(value = "id") Long id){
        if(bookService.bookByIsbn(book.getIsbn()).isPresent()) return ResponseHandler.generateResponse(HttpStatus.CONFLICT, "message", "Book with ISBN " + book.getIsbn() + " already exists");

        Optional<Book> oldBook = bookService.bookById(id);
        if(oldBook.isPresent()){
            Book newBook = oldBook.get();

            newBook.setAuthor(book.getAuthor());
            newBook.setDescription(book.getDescription());
            newBook.setGenre(book.getGenre());
            newBook.setIsbn(book.getIsbn());
            newBook.setTitle(book.getTitle());

            bookService.addBook(newBook);

            return ResponseHandler.generateResponse(HttpStatus.OK, "message", "Book edited");
        }

        logger.warn("While deleting: book with id {} not found", id);


        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "message", "Book with id " + id +" not found");
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Long id) {

        boolean result = bookService.deleteBookById(id);

        if (result) producerService.sendBookId("delete-book-topic", id.toString());

        return ResponseHandler.generateResponse(result ? HttpStatus.OK : HttpStatus.NOT_FOUND, "deleted", result);
    }
}
