package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.*;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.GenreRepository;
import ru.anastasya.readingportal.repositories.UserRepository;

import java.util.Objects;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final VolumeService volumeService;

    //потом будет через dto
    @Transactional
    public Long createBookPlaceholder(Book book, Long authorId){

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

    //пока будет такое dto, потом там будет только id книги, новое название, версия. currentUserId через авторизацию
    @Transactional
    public void changeTitle(ChangeBookTitleDTO dto){
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        checkAuthority(book, dto.currentUserId());

        if (!book.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.setTitle(dto.newTitle());
    }

    @Transactional
    public void addAuthorToBook(AddOrDeleteAuthorToFromBookDTO dto){
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        checkAuthority(book, dto.currentUserId());
        if (book.getAuthors().stream().anyMatch(u -> u.getId().equals(dto.authorId()))){
            throw new ConflictException("К книге уже добавлен этот автор");
        }
        if (!book.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        User author = userRepository.findById(dto.authorId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        book.getAuthors().add(author);
    }

    @Transactional
    public void addGenreToBook(AddOrDeleteGenreToFromBookDTO dto){
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        checkAuthority(book, dto.currentUserId());

        if (book.getGenres().stream().anyMatch(g -> g.getId().equals(dto.genreId()))){
            throw new ConflictException("К книге уже добавлен этот жанр");
        }
        Genre genre = genreRepository.findById(dto.genreId()).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        if (!book.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.getGenres().add(genre);
    }

    @Transactional
    public void deleteAuthorFromBook(AddOrDeleteAuthorToFromBookDTO dto){
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        checkAuthority(book, dto.currentUserId());
        if (book.getAuthors().stream().noneMatch(a -> a.getId().equals(dto.authorId()))){
            throw new ConflictException("К книге не добавлен этот автор");
        }
        User author = userRepository.findById(dto.authorId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        if (!book.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.getAuthors().remove(author);
    }

    @Transactional
    public void deleteGenreFromBook(AddOrDeleteGenreToFromBookDTO dto){
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        checkAuthority(book, dto.currentUserId());
        if (book.getGenres().stream().noneMatch(g -> g.getId().equals(dto.genreId()))){
            throw new ConflictException("К книге не добавлен этот жанр");
        }
        Genre genre = genreRepository.findById(dto.genreId()).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        if (!book.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
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
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        checkAuthority(book, currentUserId);

        bookRepository.delete(book);
    }

    @Transactional
    public void deleteAllBookByUserId(Long userId){
        bookRepository.deleteAllByUserId(userId);
    }

    private void checkAuthority(Book book, Long userId){
        if (book.getAuthors().stream().noneMatch(u -> u.getId().equals(userId))){
            throw new ForbiddenException("У вас нет прав");
        }
    }

}
