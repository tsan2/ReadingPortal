package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.*;
import ru.anastasya.readingportal.dto.BookFilter;
import ru.anastasya.readingportal.exception.AuthorizationException;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//добавить возможность проверки есть ли тома у книги
public class BookService {

    private final static BookService INSTANCE = new BookService();

    private BookService(){}

    public static BookService getInstance(){
        return INSTANCE;
    }

    private final BookDAO bookDAO = BookDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final GenreDAO genreDAO = GenreDAO.getInstance();
    private final ChapterService chapterService = ChapterService.getInstance();
    private final VolumeService volumeService = VolumeService.getInstance();

    public Long createBookPlaceholder(Book book, Long authorId){
        Objects.requireNonNull(book, "нельзя создать null book");

        if (book.getTitle() == null || book.getTitle().isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (book.getTitle().length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }

        Long bookId = bookDAO.save(book);
        bookDAO.addAuthorToBook(bookId, authorId);

        volumeService.createDefaultVolume(bookId, authorId);
        return bookId;
    }

    public void changeTitle(Long bookId, String newTitle, Long currentUserId){
        Objects.requireNonNull(bookId, "нельзя изменить книгу с null id");

        checkAuthority(bookId, currentUserId);

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

    public void addAuthorToBook(Long bookId, Long authorId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        if (!bookDAO.exists(bookId)){
            throw new ServiceException("Такой книги не существует");
        }
        if (!userDAO.exists(authorId)){
            throw new ServiceException("Такого пользователя не существует");
        }
        if (bookDAO.isUserAuthorOfBook(bookId, authorId)){
            throw new ServiceException("К книге уже добавлен этот автор");
        }
        bookDAO.addAuthorToBook(bookId, authorId);
    }

    public void addGenreToBook(Long bookId, Long genreId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        if (!bookDAO.exists(bookId)){
            throw new ServiceException("Такой книги не существует");
        }
        if (!genreDAO.exists(genreId)){
            throw new ServiceException("Такого жанра не существует");
        }
        if (bookDAO.isGenreAdded(bookId, genreId)){
            throw new ServiceException("К книге уже добавлен этот жанр");
        }
        bookDAO.addGenreToBook(bookId, genreId);
    }

    public void deleteAuthorFromBook(Long bookId, Long authorId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        if (!bookDAO.isUserAuthorOfBook(bookId, authorId)){
            throw new ServiceException("К книге не добавлен этот автор");
        }
        bookDAO.deleteAuthorFromBook(bookId, authorId);
    }

    public void deleteGenreFromBook(Long bookId, Long genreId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
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
        if (!books.isEmpty()){
            HashMap<Long, List<User>> authorsMap = userDAO.findAllAuthorsOfBooks(books);
            HashMap<Long, List<Genre>> genresMap = genreDAO.findAllGenresOfBooks(books);
            for (Book book : books){
                book.setAuthors(authorsMap.get(book.getId()));
                book.setGenres(genresMap.get(book.getId()));
            }
        }
        return books;
    }

    public Book findById(Long id){
        return bookDAO.findById(id);
    }

    public void deleteBook(Long bookId, Long currentUserId){
        checkAuthority(bookId, currentUserId);

        List<Volume> volumes = volumeService.findAllByBookId(bookId);
        for (Volume volume : volumes){
            chapterService.deleteAllChapter(volume.getId(), currentUserId);
        }
        volumeService.deleteAllVolume(bookId, currentUserId);
        volumeService.deleteDefaultVolume(bookId, currentUserId);
        bookDAO.deleteAllGenresFromBook(bookId);
        bookDAO.deleteAllAuthorsFromBook(bookId);
        bookDAO.delete(bookId);
    }

    public void deleteAllBookByUserId(Long userId){
        BookFilter bookFilter = new BookFilter(userId, null, null);
        List<Book> books = findFullBooksByBookFilter(bookFilter);

        for (Book book : books){
            if (book.getAuthors().size() == 1){
                deleteBook(book.getId(), userId);
            }
        }
    }

    private void checkAuthority(Long bookId, Long userId){
        if (!bookDAO.isUserAuthorOfBook(bookId, userId)){
            throw new AuthorizationException("У вас нет прав");
        }
    }
}
