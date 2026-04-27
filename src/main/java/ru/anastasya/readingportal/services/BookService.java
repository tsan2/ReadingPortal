package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.BookFilter;
import ru.anastasya.readingportal.dto.BookSummaryDTO;
import ru.anastasya.readingportal.exception.*;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.GenreRepository;
import ru.anastasya.readingportal.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final VolumeService volumeService;

    @Transactional
    public Long createBookPlaceholder(Book book, Long authorId){
        Objects.requireNonNull(book, "нельзя создать null book");

        if (book.getTitle() == null || book.getTitle().isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if (book.getTitle().length()>250){
            throw new ValidationException("Название не может быть длиннее 250 символов");
        }
        User author = userRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        book.getAuthors().add(author);
        bookRepository.save(book);
        volumeService.createDefaultVolume(book.getId(), authorId);
        return book.getId();
    }

    @Transactional
    public void changeTitle(Long bookId, String newTitle, Long currentUserId){
        Objects.requireNonNull(bookId, "нельзя изменить книгу с null id");

        checkAuthority(bookId, currentUserId);

        if (newTitle == null || newTitle.isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if (newTitle.length()>250){
            throw new ValidationException("Название не может быть длиннее 250 символов");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        book.setTitle(newTitle);
    }

    @Transactional
    public void addAuthorToBook(Long bookId, Long authorId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        if (!bookRepository.existsById(bookId)){
            throw new EntityNotFoundException("Такой книги не существует");
        }
        if (!userRepository.existsById(authorId)){
            throw new EntityNotFoundException("Такого пользователя не существует");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        if (book.getAuthors().stream().anyMatch(u -> u.getId().equals(currentUserId))){
            throw new ConflictException("К книге уже добавлен этот автор");
        }
        User author = userRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        book.getAuthors().add(author);
    }

    @Transactional
    public void addGenreToBook(Long bookId, Long genreId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        if (!bookRepository.existsById(bookId)){
            throw new EntityNotFoundException("Такой книги не существует");
        }
        if (!genreRepository.existsById(genreId)){
            throw new EntityNotFoundException("Такого жанра не существует");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        if (book.getGenres().stream().anyMatch(g -> g.getId().equals(genreId))){
            throw new ConflictException("К книге уже добавлен этот жанр");
        }
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        book.getGenres().add(genre);
    }

    @Transactional
    public void deleteAuthorFromBook(Long bookId, Long authorId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        if (book.getAuthors().stream().noneMatch(a -> a.getId().equals(authorId))){
            throw new ConflictException("К книге не добавлен этот автор");
        }
        User author = userRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        book.getAuthors().remove(author);
    }

    @Transactional
    public void deleteGenreFromBook(Long bookId, Long genreId, Long currentUserId){
        checkAuthority(bookId, currentUserId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        if (book.getGenres().stream().noneMatch(g -> g.getId().equals(genreId))){
            throw new ConflictException("К книге не добавлен этот жанр");
        }
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        book.getGenres().add(genre);
    }

    //может ещё загружать список авторов(и возможно жанров)
    @Transactional(readOnly = true)
    public Page<BookSummaryDTO> findBooksByBookFilter(BookFilter bookFilter, int size, int page){
        Sort sort = null;
        switch (bookFilter.getBookSortStrategy()){
            case NEWEST -> sort = Sort.by("createdAt").descending();
            case ALPHABETICAL -> sort = Sort.by("title").ascending();
        }
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Book> books = bookRepository.findBooksByBookFilter(bookFilter.getAuthorsIds(), bookFilter.getGenresIds(), pageable);
        return books.map(b -> new BookSummaryDTO(b.getId(), b.getTitle()));
    }

    //скорее всего не нужно
//    public List<Book> findFullBooksByBookFilter(BookFilter bookFilter){
//        List<Book> books = bookDAO.findBooksByBookFilter(bookFilter);
//        if (!books.isEmpty()){
//            HashMap<Long, List<User>> authorsMap = userDAO.findAllAuthorsOfBooks(books);
//            HashMap<Long, List<Genre>> genresMap = genreDAO.findAllGenresOfBooks(books);
//
//            for (Book book : books){
//                book.setAuthors(authorsMap.get(book.getId()));
//                book.setGenres(genresMap.get(book.getId()));
//            }
//        }
//        return books;
//    }

    @Transactional(readOnly = true)
    public Book findById(Long id){
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBook(Long bookId, Long currentUserId){
        checkAuthority(bookId, currentUserId);

        bookRepository.deleteById(bookId);
    }

    @Transactional
    public void deleteAllBookByUserId(Long userId){
        bookRepository.deleteAllByUserId(userId);
    }

    private void checkAuthority(Long bookId, Long userId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        if (book.getAuthors().stream().noneMatch(u -> u.getId().equals(userId))){
            throw new ForbiddenException("У вас нет прав");
        }
    }

}
