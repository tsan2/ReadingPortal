package ru.anastasya.readingportal.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.GenreRequestDTO;
import ru.anastasya.readingportal.dto.GenreResponseDTO;
import ru.anastasya.readingportal.exceptions.EntityNotFoundException;
import ru.anastasya.readingportal.exceptions.ValidationException;
import ru.anastasya.readingportal.mappers.GenreMapper;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.services.GenreService;

@AllArgsConstructor
@RestController
@RequestMapping("/genre")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @PostMapping("")
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody @Valid GenreRequestDTO genreRequestDTO){
        GenreResponseDTO genreResponseDTO = genreService.createGenre(genreRequestDTO);
        return new ResponseEntity<>(genreResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id){
        if (!genreService.existsById(id)){
            throw new EntityNotFoundException("Жанр не найден");
        }
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> findById(@PathVariable Long id){
        Genre genre = genreService.findById(id);
        GenreResponseDTO genreResponseDTO = genreMapper.toGenreResponseDTO(genre);
        return new ResponseEntity<>(genreResponseDTO, HttpStatus.OK);
    }

    @GetMapping(params = "name")
    public ResponseEntity<GenreResponseDTO> findByName(@RequestParam String name){
        Genre genre = genreService.findByName(name);
        GenreResponseDTO genreResponseDTO = genreMapper.toGenreResponseDTO(genre);
        return new ResponseEntity<>(genreResponseDTO, HttpStatus.OK);
    }

    @GetMapping("")
    public Page<GenreResponseDTO> findAll(@RequestParam int size, @RequestParam int page){
        return genreService.findAll(size, page).map(genreMapper::toGenreResponseDTO);
    }

}
