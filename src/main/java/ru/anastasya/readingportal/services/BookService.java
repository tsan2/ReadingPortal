package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.*;
import ru.anastasya.readingportal.mappers.BookMapper;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.GenreRepository;
import ru.anastasya.readingportal.repositories.UserRepository;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final VolumeService volumeService;
    private final BookMapper bookMapper;

    @Transactional
    public BookResponseDTO createBookPlaceholder(CreateBookDTO dto, Long authorId){
        User author = userRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        Book book = bookMapper.fromCreateBookDTO(dto);
        book.getAuthors().add(author);
        Book bookNew = bookRepository.save(book);
        volumeService.createDefaultVolume(book.getId());
        return bookMapper.toBookResponseDTO(bookNew);
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByBookId(#bookId, principal.id)")
    @Transactional
    public BookResponseDTO changeTitle(ChangeTitleDTO dto, Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        if (!book.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.setTitle(dto.newTitle());
        return bookMapper.toBookResponseDTO(book);
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByBookId(#bookId, principal.id)")
    @Transactional
    public BookResponseDTO addAuthorToBook(Long bookId, Long authorId, int version){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        if (book.getAuthors().stream().anyMatch(u -> u.getId().equals(authorId))){
            throw new ConflictException("К книге уже добавлен этот автор");
        }
        if (!book.getVersion().equals(version)){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        User author = userRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        book.getAuthors().add(author);
        return bookMapper.toBookResponseDTO(book);
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByBookId(#bookId, principal.id)")
    @Transactional
    public BookResponseDTO addGenreToBook(Long bookId, Long genreId, int version){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        if (book.getGenres().stream().anyMatch(g -> g.getId().equals(genreId))){
            throw new ConflictException("К книге уже добавлен этот жанр");
        }
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        if (!book.getVersion().equals(version)){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.getGenres().add(genre);
        return bookMapper.toBookResponseDTO(book);
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByBookId(#bookId, principal.id)")
    @Transactional
    public BookResponseDTO deleteAuthorFromBook(Long bookId, Long authorId, int version){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        if (book.getAuthors().stream().noneMatch(a -> a.getId().equals(authorId))){
            throw new ConflictException("К книге не добавлен этот автор");
        }
        User author = userRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        if (!book.getVersion().equals(version)){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.getAuthors().remove(author);
        return bookMapper.toBookResponseDTO(book);
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByBookId(#bookId, principal.id)")
    @Transactional
    public BookResponseDTO deleteGenreFromBook(Long bookId, Long genreId, int version){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        if (book.getGenres().stream().noneMatch(g -> g.getId().equals(genreId))){
            throw new ConflictException("К книге не добавлен этот жанр");
        }
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        if (!book.getVersion().equals(version)){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        book.getGenres().remove(genre);
        return bookMapper.toBookResponseDTO(book);
    }

    @Transactional(readOnly = true)
    public Page<BookSummaryDTO> findBooksByBookFilter(BookFilter bookFilter, int size, int page){
        Sort sort = Sort.unsorted();
        if (bookFilter.getBookSortStrategy() != null){
            switch (bookFilter.getBookSortStrategy()){
                case NEWEST -> sort = Sort.by("createdAt").descending();
                case ALPHABETICAL -> sort = Sort.by("title").ascending();
            }
        }
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Book> books = bookRepository.findBooksByBookFilter(bookFilter.getTitle(), bookFilter.getAuthorsIds(), bookFilter.getGenresIds(), pageable);
        return books.map(b -> new BookSummaryDTO(b.getId(), b.getTitle()));
    }

    @Transactional(readOnly = true)
    public BookResponseDTO findById(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        return bookMapper.toBookResponseDTO(book);
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByBookId(#bookId, principal.id)")
    @Transactional
    public void deleteBook(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        bookRepository.delete(book);
    }

    @Transactional
    public void deleteAllBookByUserId(Long userId){
        bookRepository.deleteAllByUserId(userId);
    }


}
