package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.security.CustomUserDetails;
import ru.anastasya.readingportal.services.VolumeService;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@Tag(name = "Тома", description = "Методы для работы с томами")
@RequestMapping("")
public class VolumeController {

    private VolumeService volumeService;

    @ApiResponse(responseCode = "404", description = "Книга, к которой вы хотите создать том, не найдена")
    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Такой номер тома уже существует")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Создать том")
    @PostMapping("/book/{bookId}/volume")
    public ResponseEntity<VolumeResponseDTO> createVolume(@RequestBody VolumeRequest volumeRequest,
                                                          @Parameter(description = "айди книги", example = "1")
                                                          @PathVariable @Min(1) Long bookId){
        VolumeResponseDTO volumeResponseDTO = volumeService.createVolume(volumeRequest, bookId);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Том не найден")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Изменить название или номер тома")
    @PatchMapping("volume/{id}")
    public ResponseEntity<VolumeResponseDTO> updateVolume(@RequestBody UpdateVolumeDTO updateVolumeDTO,
                                                          @Parameter(description = "айди тома", example = "1")
                                                          @PathVariable @Min(1) Long id){
        VolumeResponseDTO volumeResponseDTO = volumeService.updateVolume(updateVolumeDTO, id);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Найти все тома по айди книги")
    @GetMapping("/book/{bookId}/volume")
    public ResponseEntity<List<VolumeSummaryDTO>> findAllByBookId(@Parameter(description = "айди книги", example = "1")
                                                                  @PathVariable @Min(1) Long bookId){
        List<VolumeSummaryDTO> volumes = volumeService.findAllByBookId(bookId);
        return new ResponseEntity<>(volumes, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "404", description = "Том не найден")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Найти том по айди")
    @GetMapping("/volume/{id}")
    public ResponseEntity<VolumeResponseDTO> findById(@Parameter(description = "айди тома", example = "1")
                                                      @PathVariable @Min(1) Long id){
        VolumeResponseDTO volumeResponseDTO = volumeService.findById(id);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "204", description = "Объект успешно удален")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Том не найден")
    @ApiResponse(responseCode = "401", description = "Вы не авторизованы")
    @Operation(summary = "Удалить том")
    @DeleteMapping("/volume/{id}")
    public ResponseEntity<Void> deleteVolume(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Parameter(description = "айди тома", example = "1")
                                             @PathVariable @Min(1) Long id){
        volumeService.deleteVolume(id);
        return ResponseEntity.noContent().build();
    }
}
