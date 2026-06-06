package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.security.CustomUserDetails;
import ru.anastasya.readingportal.services.BookService;

@AllArgsConstructor
@RestController
@Validated
@Tag(name = "Книги",
description = "Методы для работы с книгами")
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    //Временно айди автора через параметры

    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @Operation(summary = "Создать книгу")
    @PostMapping("")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody CreateBookDTO createBookDTO,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        BookResponseDTO bookResponseDTO = bookService.createBookPlaceholder(createBookDTO, userDetails.getId());
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные")
    @Operation(summary = "Изменить название книги")
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> changeTitle(@Valid @RequestBody ChangeTitleDTO bookTitleDTO,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @Parameter(description = "айди книги", example = "1")
                                                       @PathVariable @Min(1) Long id){
        BookResponseDTO bookResponseDTO = bookService.changeTitle(bookTitleDTO, userDetails.getId(), id);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "201", description = "Автор добавлен")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные или к книге уже добавлен этот автор")
    @Operation(summary = "Добавить автора к книге")
    @PostMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDTO> addAuthor(@Parameter(description = "айди книги", example = "1")
                                                     @PathVariable @Min(1) Long bookId,
                                                     @Parameter(description = "айди автора", example = "1")
                                                     @PathVariable @Min(1) Long authorId,
                                                     @Parameter(description = "версия записи", example = "1")
                                                     @RequestParam @Min(0) int version,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails){
        BookResponseDTO bookResponseDTO = bookService.addAuthorToBook(bookId, authorId, version, userDetails.getId());
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "201", description = "Жанр добавлен")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные или к книге уже добавлен этот жанр")
    @Operation(summary = "Добавить жанр к книге")
    @PostMapping("/{bookId}/genre/{genreId}")
    public ResponseEntity<BookResponseDTO> addGenre(@Parameter(description = "айди книги", example = "1")
                                                    @PathVariable @Min(1) Long bookId,
                                                    @Parameter(description = "айди жанра", example = "1")
                                                    @PathVariable @Min(1) Long genreId,
                                                    @Parameter(description = "версия записи", example = "1")
                                                    @RequestParam @Min(0) int version,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        BookResponseDTO bookResponseDTO = bookService.addGenreToBook(bookId, genreId, version, userDetails.getId());
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }


    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные или к книге не добавлен этот автор")
    @Operation(summary = "Удалить автора из книги")
    @DeleteMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDTO> deleteAuthor(@Parameter(description = "айди книги", example = "1")
                                                        @PathVariable @Min(1) Long bookId,
                                                        @Parameter(description = "айди автора", example = "1")
                                                        @PathVariable @Min(1) Long authorId,
                                                        @Parameter(description = "версия записи", example = "1")
                                                        @RequestParam @Min(0) int version,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        BookResponseDTO bookResponseDTO = bookService.deleteAuthorFromBook(bookId, authorId, version, userDetails.getId());
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные или к книге не добавлен этот жанр")
    @Operation(summary = "Удалить жанр из книги")
    @DeleteMapping("/{bookId}/genre/{genreId}")
    public ResponseEntity<BookResponseDTO> deleteGenre(@Parameter(description = "айди книги", example = "1")
                                                       @PathVariable @Min(1) Long bookId,
                                                       @Parameter(description = "айди жанра", example = "1")
                                                       @PathVariable @Min(1) Long genreId,
                                                       @Parameter(description = "версия записи", example = "1")
                                                       @RequestParam @Min(0) int version,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails){
        BookResponseDTO bookResponseDTO = bookService.deleteGenreFromBook(bookId, genreId, version, userDetails.getId());
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Найти книги")
    @GetMapping("")
    public ResponseEntity<Page<BookSummaryDTO>> findByBookFilter(@ParameterObject BookFilterRequestDTO bookFilterRequestDTO,
                                                                 @Parameter(description = "номер страницы (начинается с 1)",
                                                                         example = "1")
                                                                 @RequestParam @Min(1) int page,
                                                                 @Parameter(description = "размер страницы", example = "10")
                                                                 @RequestParam @Min(1) int size){
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

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Книга не найдена")
    @Operation(summary = "Найти книгу по айди")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findById(@Parameter(description = "айди книги", example = "1")
                                                    @PathVariable @Min(1) Long id){
        BookResponseDTO bookResponseDTO = bookService.findById(id);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "204", description = "Объект успешно удален")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Книга не найдена")
    @Operation(summary = "Удалить книгу")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @Parameter(description = "айди книги", example = "1")
                                       @PathVariable @Min(1) Long id){
        bookService.deleteBook(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

}
