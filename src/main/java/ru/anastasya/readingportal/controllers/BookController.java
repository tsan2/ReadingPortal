package ru.anastasya.readingportal.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.services.BookService;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    //Временно айди автора через параметры
    @PostMapping("")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody CreateBookDTO createBookDTO, @RequestParam Long authorId){
        BookResponseDTO bookResponseDTO = bookService.createBookPlaceholder(createBookDTO, authorId);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/change-title")
    public ResponseEntity<BookResponseDTO> changeTitle(@Valid @RequestBody ChangeBookTitleDTO bookTitleDTO,
                                                       @RequestParam Long currentUserId){
        BookResponseDTO bookResponseDTO = bookService.changeTitle(bookTitleDTO, currentUserId);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDTO> addAuthor(@PathVariable Long bookId,
                                                     @PathVariable Long authorId,
                                                     @RequestParam int version,
                                                     @RequestParam Long currentUserId){
        BookResponseDTO bookResponseDTO = bookService.addAuthorToBook(bookId, authorId, version, currentUserId);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{bookId}/genre/{genreId}")
    public ResponseEntity<BookResponseDTO> addGenre(@PathVariable Long bookId,
                                                     @PathVariable Long genreId,
                                                     @RequestParam int version,
                                                     @RequestParam Long currentUserId){
        BookResponseDTO bookResponseDTO = bookService.addGenreToBook(bookId, genreId, version, currentUserId);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDTO> deleteAuthor(@PathVariable Long bookId,
                                                     @PathVariable Long authorId,
                                                     @RequestParam int version,
                                                     @RequestParam Long currentUserId){
        BookResponseDTO bookResponseDTO = bookService.deleteAuthorFromBook(bookId, authorId, version, currentUserId);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{bookId}/genre/{genreId}")
    public ResponseEntity<BookResponseDTO> deleteGenre(@PathVariable Long bookId,
                                                        @PathVariable Long genreId,
                                                        @RequestParam int version,
                                                        @RequestParam Long currentUserId){
        BookResponseDTO bookResponseDTO = bookService.deleteGenreFromBook(bookId, genreId, version, currentUserId);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<BookSummaryDTO>> findByBookFilter(BookFilterRequestDTO bookFilterRequestDTO,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size){
        BookSortStrategy bookSortStrategy = null;
        if (bookFilterRequestDTO.sortStrategy() != null
                && bookFilterRequestDTO.sortStrategy().equalsIgnoreCase("alphabetical")){
            bookSortStrategy = BookSortStrategy.ALPHABETICAL;
        }
        else if (bookFilterRequestDTO.sortStrategy() != null
                && bookFilterRequestDTO.sortStrategy().equalsIgnoreCase("newest")){
            bookSortStrategy = BookSortStrategy.NEWEST;
        }
        BookFilter bookFilter = new BookFilter(
                bookFilterRequestDTO.title(),
                bookFilterRequestDTO.authorsIds(),
                bookFilterRequestDTO.genresIds(),
                bookSortStrategy);

        Page<BookSummaryDTO> books = bookService.findBooksByBookFilter(bookFilter, size, page);

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findById(@PathVariable Long id){
        BookResponseDTO bookResponseDTO = bookService.findById(id);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long currentUserId){
        bookService.deleteBook(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

}
