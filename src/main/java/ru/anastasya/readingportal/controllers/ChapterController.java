package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.services.ChapterService;

import java.util.List;

@Tag(name = "Главы", description = "Методы для работы с главами")
@Validated
@AllArgsConstructor
@RestController
public class ChapterController {

    private final ChapterService chapterService;

    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Том с таким айди не найден")
    @ApiResponse(responseCode = "409", description = "Такой номер главы уже существует")
    @ApiResponse(responseCode = "403", description = "У вас нет прав")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Создать главу внутри тома")
    @PostMapping("/volume/{volumeId}/chapter")
    public ResponseEntity<ChapterShortResponseDTO> createChapterPlaceholderInVolume(@RequestBody @Valid ChapterCreateDTO dto,
                                                                                   @PathVariable @Min(1) Long volumeId){
        ChapterShortResponseDTO responseDTO = chapterService
                .createChapterPlaceHolder(dto, null, volumeId);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Книга с таким айди не найдена")
    @ApiResponse(responseCode = "409", description = "Такой номер главы уже существует")
    @ApiResponse(responseCode = "403", description = "У вас нет прав")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Создать главу внутри книги (без тома)")
    @PostMapping("/book/{bookId}/chapter")
    public ResponseEntity<ChapterShortResponseDTO> createChapterPlaceholderInBook(@RequestBody @Valid ChapterCreateDTO dto,
                                                                                    @PathVariable @Min(1) Long bookId){
        ChapterShortResponseDTO responseDTO = chapterService
                .createChapterPlaceHolder(dto, bookId, null);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Глава с таким айди не найдена")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные. Попробуйте ещё раз")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Добавить текст к главе")
    @PutMapping("/chapter/{id}/content")
    public ResponseEntity<ChapterFullDTO> addContent(@RequestBody @Valid ChapterAddContentDTO dto,
                                                     @PathVariable @Min(1) Long id){
        ChapterFullDTO responseDTO = chapterService.addContent(dto, id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Глава с таким айди не найдена")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные. Попробуйте ещё раз или такой номер главы уже существует")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Изменить название или номер главы")
    @PatchMapping("/chapter/{id}")
    public ResponseEntity<ChapterShortResponseDTO> update(@RequestBody @Valid ChapterUpdateDTO dto,
                                                          @PathVariable @Min(1) Long id){
        ChapterShortResponseDTO responseDTO = chapterService.update(id, dto);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "У этой книги главы находятся внутри томов")
    @ApiResponse(responseCode = "404", description = "Книга с таким айди не найдена")
    @Operation(summary = "Найти все главы в книге")
    @GetMapping("/book/{id}/chapter")
    public ResponseEntity<List<ChapterShortDTO>> findAllShortInBook(@PathVariable @Min(1) Long id){
        FindAllShortChapterDTO findAllShortChapterDTO = new FindAllShortChapterDTO(id, null);
        List<ChapterShortDTO> responseList = chapterService.findAllShortByVolumeIdOrBookId(findAllShortChapterDTO);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Том с таким айди не найден")
    @Operation(summary = "Найти все главы в томе")
    @GetMapping("/volume/{id}/chapter")
    public ResponseEntity<List<ChapterShortDTO>> findAllShortInVolume(@PathVariable @Min(1) Long id){
        FindAllShortChapterDTO findAllShortChapterDTO = new FindAllShortChapterDTO(null, id);
        List<ChapterShortDTO> responseList = chapterService.findAllShortByVolumeIdOrBookId(findAllShortChapterDTO);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "404", description = "Глава не найдена")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @GetMapping("/chapter/{id}")
    @Operation(summary = "Найти главу по айди")
    public ResponseEntity<ChapterFullDTO> findById(@PathVariable @Min(1) Long id){
        ChapterFullDTO responseDTO = chapterService.findFullById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "204", description = "Объект успешно удален")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Глава не найдена")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @DeleteMapping("/chapter/{id}")
    @Operation(summary = "Удалить главу")
    public ResponseEntity<?> deleteChapter(@PathVariable @Min(1) Long id){
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}
