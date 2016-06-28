package com.example.controllers;

import com.example.models.Book;
import com.example.models.ValidationError;
import com.example.models.ValidationErrors;
import com.example.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("could not find book '" + bookId);
    }
}

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MessageSource msgSource;

    private Book findBookByIdOrThrow(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> processNotFoundError(BookNotFoundException ex) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException ex) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream().map((error) -> {
            String msg = msgSource.getMessage(error.getDefaultMessage(), null, currentLocale);
            return new ValidationError(msg);
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new ValidationErrors(errors), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Book>> getAllBooks() {
        return new ResponseEntity<>((Collection<Book>) bookRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        Book book = findBookByIdOrThrow(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addBook(@Validated @RequestBody Book input) {
        return new ResponseEntity<>(bookRepository.save(input), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBook(@Validated @RequestBody Book input, @PathVariable("id") Long id) {
        Book book = findBookByIdOrThrow(id);
        book.setTitle(input.getTitle()).setAuthors(input.getAuthors());
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        bookRepository.delete(findBookByIdOrThrow(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
