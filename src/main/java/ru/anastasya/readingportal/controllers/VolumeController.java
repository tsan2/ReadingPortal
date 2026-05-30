package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.services.VolumeService;
import ru.anastasya.readingportal.utils.OperationResult;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("")
public class VolumeController {

    private VolumeService volumeService;

    @ApiResponse(responseCode = "404", description = "Книга, к которой вы хотите создать том, не найдена")
    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Такой номер тома уже существует")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @Operation(summary = "Создать том")
    @PostMapping("/book/{id}/volume")
    public ResponseEntity<VolumeResponseDTO> createVolume(@RequestBody VolumeRequest volumeRequest,
                                                          @Parameter(description = "айди текущего пользователя", example = "1")
                                                          @RequestParam @Min(1) Long currentUserId,
                                                          @Parameter(description = "айди книги", example = "1")
                                                          @PathVariable @Min(1) Long bookId){
        VolumeResponseDTO volumeResponseDTO = volumeService.createVolume(volumeRequest, currentUserId, bookId);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.CREATED);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Том не найден")
    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение действия")
    @Operation(summary = "Изменить название или номер тома")
    @PatchMapping("volume/{id}")
    public ResponseEntity<VolumeResponseDTO> updateVolume(@RequestBody UpdateVolumeDTO updateVolumeDTO,
                                                          @Parameter(description = "айди текущего пользователя", example = "1")
                                                          @RequestParam @Min(1) Long currentUserId,
                                                          @Parameter(description = "айди тома", example = "1")
                                                          @PathVariable @Min(1) Long id){
        VolumeResponseDTO volumeResponseDTO = volumeService.updateVolume(updateVolumeDTO, currentUserId, id);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Найти все тома по айди книги")
    @GetMapping("/book/{id}/volume")
    public ResponseEntity<List<VolumeSummaryDTO>> findAllByBookId(@Parameter(description = "айди книги", example = "1")
                                                                  @PathVariable @Min(1) Long id){
        List<VolumeSummaryDTO> volumes = volumeService.findAllByBookId(id);
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
    @Operation(summary = "Удалить том")
    @DeleteMapping("/volume/{id}")
    public ResponseEntity<Void> deleteVolume(@Parameter(description = "айди текущего пользователя", example = "1")
                                             @RequestParam @Min(1) Long currentUserId,
                                             @Parameter(description = "айди тома", example = "1")
                                             @PathVariable @Min(1) Long id){
        volumeService.deleteVolume(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
