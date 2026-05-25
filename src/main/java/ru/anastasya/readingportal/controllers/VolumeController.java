package ru.anastasya.readingportal.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.services.VolumeService;
import ru.anastasya.readingportal.utils.OperationResult;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("")
public class VolumeController {

    private VolumeService volumeService;

    @PostMapping("/book/{id}/volume")
    public ResponseEntity<VolumeResponseDTO> createVolume(@RequestBody VolumeRequest volumeRequest,
                                                          @RequestParam Long currentUserId,
                                                          @PathVariable Long bookId){
        VolumeResponseDTO volumeResponseDTO = volumeService.createVolume(volumeRequest, currentUserId, bookId);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.CREATED);
    }

    @PatchMapping("volume/{id}")
    public ResponseEntity<VolumeResponseDTO> updateVolume(@RequestBody UpdateVolumeDTO updateVolumeDTO,
                                                          @RequestParam Long currentUserId,
                                                          @PathVariable Long id){
        VolumeResponseDTO volumeResponseDTO = volumeService.updateVolume(updateVolumeDTO, currentUserId, id);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/book/{id}/volume")
    public ResponseEntity<List<VolumeSummaryDTO>> findAllByBookId(@PathVariable Long id){
        List<VolumeSummaryDTO> volumes = volumeService.findAllByBookId(id);
        return new ResponseEntity<>(volumes, HttpStatus.OK);
    }

    @GetMapping("/volume/{id}")
    public ResponseEntity<VolumeResponseDTO> findById(@PathVariable Long id){
        VolumeResponseDTO volumeResponseDTO = volumeService.findById(id);
        return new ResponseEntity<>(volumeResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/volume/{id}")
    public ResponseEntity<Void> deleteVolume(@PathVariable Long id,
                                             @RequestParam Long currentUserId){
        volumeService.deleteVolume(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
