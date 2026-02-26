package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.*;
import ru.anastasya.readingportal.dto.BookFilter;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BookService {

    private final BookDAO bookDAO;
    private final UserDAO userDAO;
    private final GenreDAO genreDAO;
    private final ChapterService chapterService;
    private final VolumeService volumeService;

    public BookService(BookDAO bookDAO, UserDAO userDAO, GenreDAO genreDAO, ChapterService chapterService, VolumeService volumeService) {
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
        this.genreDAO = genreDAO;
        this.chapterService = chapterService;
        this.volumeService = volumeService;
    }

    public void createBookPlaceholder(Book book, Long authorId){
        Objects.requireNonNull(book, "нельзя создать null book");

        if (book.getTitle() == null || book.getTitle().isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (book.getTitle().length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }

        Long bookId = bookDAO.save(book);
        bookDAO.addAuthorToBook(bookId, authorId);

        volumeService.createDefaultVolume(bookId);
    }

    public void changeTitle(Long bookId, String newTitle){
        Objects.requireNonNull(bookId, "нельзя изменить книгу с null id");
        if (newTitle == null || newTitle.isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (newTitle.length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }

        Book book = bookDAO.findById(bookId);
        if (book == null){
            throw new ServiceException("Книга не найдена");
        }
        book.setTitle(newTitle);

        bookDAO.update(book);
    }

    public void addAuthorToBook(Long bookId, Long authorId){
        if (bookDAO.isAuthorAdded(bookId, authorId)){
            throw new ServiceException("К книге уже добавлен этот автор");
        }
        bookDAO.addAuthorToBook(bookId, authorId);
    }

    public void addGenreToBook(Long bookId, Long genreId){
        if (bookDAO.isGenreAdded(bookId, genreId)){
            throw new ServiceException("К книге уже добавлен этот жанр");
        }
        bookDAO.addGenreToBook(bookId, genreId);
    }

    public void deleteAuthorFromBook(Long bookId, Long authorId){
        if (!bookDAO.isAuthorAdded(bookId, authorId)){
            throw new ServiceException("К книге не добавлен этот автор");
        }
        bookDAO.deleteAuthorFromBook(bookId, authorId);
    }

    public void deleteGenreFromBook(Long bookId, Long genreId){
        if (bookDAO.isGenreAdded(bookId, genreId)){
            throw new ServiceException("К книге не добавлен этот жанр");
        }
        bookDAO.deleteGenreFromBook(bookId, genreId);
    }

    public List<Book> findBooksByBookFilter(BookFilter bookFilter){
        return bookDAO.findBooksByBookFilter(bookFilter);
    }

    public List<Book> findFullBooksByBookFilter(BookFilter bookFilter){
        List<Book> books = bookDAO.findBooksByBookFilter(bookFilter);
        HashMap<Long, List<User>> authorsMap = userDAO.findAllAuthorsOfBooks(books);
        HashMap<Long, List<Genre>> genresMap = genreDAO.findAllGenresOfBooks(books);
        for (Book book : books){
            book.setAuthors(authorsMap.get(book.getId()));
            book.setGenres(genresMap.get(book.getId()));
        }
        return books;
    }

    public void deleteBook(Long bookId){
        bookDAO.deleteAllGenresFromBook(bookId);
        bookDAO.deleteAllAuthorsFromBook(bookId);
        List<Volume> volumes = volumeService.findAllByBookId(bookId);
        for (Volume volume : volumes){
            chapterService.deleteAllChapter(volume.getId());
        }
        volumeService.deleteAllVolume(bookId);
        volumeService.deleteDefaultVolume(bookId);
        bookDAO.delete(bookId);
    }

}
