package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.exception.EntityNotFoundException;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.repositories.BookRepository;

import java.util.Objects;

public class BookService {

    BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    private final BookRepository bookRepository;

//    public Long createBookPlaceholder(Book book, Long authorId){
//        Objects.requireNonNull(book, "нельзя создать null book");
//
//        if (book.getTitle() == null || book.getTitle().isBlank()){
//            throw new ValidationException("Название не может быть пустым");
//        }
//        if (book.getTitle().length()>250){
//            throw new ValidationException("Название не может быть длиннее 250 символов");
//        }
//
//        Long bookId = bookRepository.save(book).getId();
//        bookDAO.addAuthorToBook(bookId, authorId);
//
//        volumeService.createDefaultVolume(bookId, authorId);
//        return bookId;
//    }
//
//    public void changeTitle(Long bookId, String newTitle, Long currentUserId){
//        Objects.requireNonNull(bookId, "нельзя изменить книгу с null id");
//
//        checkAuthority(bookId, currentUserId);
//
//        if (newTitle == null || newTitle.isBlank()){
//            throw new ValidationException("Название не может быть пустым");
//        }
//        if (newTitle.length()>250){
//            throw new ValidationException("Название не может быть длиннее 250 символов");
//        }
//
//        Book book = bookDAO.findById(bookId);
//        if (book == null){
//            throw new EntityNotFoundException("Книга не найдена");
//        }
//        book.setTitle(newTitle);
//
//        bookDAO.update(book);
//    }

}
