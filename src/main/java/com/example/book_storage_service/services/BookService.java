package com.example.book_storage_service.services;

import com.example.book_storage_service.models.Book;
import com.example.book_storage_service.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    final private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<Book> bookByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    public Optional<Book> bookById(Long id){
        return bookRepository.findById(id);
    }

    public List<Book> allBooks(){
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public boolean deleteBookById(Long id){
        Optional<Book> book = bookRepository.findById(id);

        if(book.isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        System.out.println("While deleting: book with id " + id +" have not found");
        return false;
    }


}