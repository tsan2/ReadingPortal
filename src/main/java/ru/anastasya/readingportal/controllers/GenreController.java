package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.GenreRequestDTO;
import ru.anastasya.readingportal.dto.GenreResponseDTO;
import ru.anastasya.readingportal.mappers.GenreMapper;
import ru.anastasya.readingportal.services.GenreService;

@Tag(name = "Жанры",
description = "Методы для работы с жанрами")
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/genre")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @ApiResponse(responseCode = "403", description = "У вас недостаточно прав")
    @Operation(summary = "Создать жанр")
    @PostMapping("")
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody @Valid GenreRequestDTO genreRequestDTO){
        GenreResponseDTO genreResponseDTO = genreService.createGenre(genreRequestDTO);
        return new ResponseEntity<>(genreResponseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "204", description = "Объект успешно удален")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Жанр не найден")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @ApiResponse(responseCode = "403", description = "У вас недостаточно прав")
    @Operation(summary = "Удалить жанр")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@Parameter(description = "айди жанра", example = "1")
                                            @PathVariable @Min(1) Long id){
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Жанр не найден")
    @Operation(summary = "Найти жанр по айди")
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> findById(@Parameter(description = "айди жанра", example = "1")
                                                     @PathVariable @Min(1) Long id){
        GenreResponseDTO genreResponseDTO = genreService.findById(id);
        return new ResponseEntity<>(genreResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Жанр не найден")
    @Operation(summary = "Найти жанр по названию")
    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<GenreResponseDTO> findByName(@Parameter(description = "название жанра", example = "хоррор")
                                                       @RequestParam @NotBlank String name){
        GenreResponseDTO genreResponseDTO = genreService.findByName(name);
        return new ResponseEntity<>(genreResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Получить список жанров")
    @GetMapping("")
    public ResponseEntity<Page<GenreResponseDTO>> findAll(@Parameter(description = "номер страницы (начинается с 1)", example = "1")
                                          @RequestParam @Min(1) int page,
                                          @Parameter(description = "размер страницы", example = "10")
                                          @RequestParam @Min(1) int size){
        Page<GenreResponseDTO> genreResponseDTOS = genreService.findAll(size, page);
        return new ResponseEntity<>(genreResponseDTOS, HttpStatus.OK);
    }

}
