package ru.anastasya.readingportal.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.GenreRequestDTO;
import ru.anastasya.readingportal.dto.GenreResponseDTO;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.services.GenreService;

@RestController
@RequestMapping("/genre")
public class GenreController {

    GenreController(GenreService genreService){
        this.genreService = genreService;
    }

    private final GenreService genreService;

    @PostMapping("")
    public ResponseEntity<String> createGenre(@RequestBody GenreRequestDTO genreRequestDTO){
        if (genreRequestDTO.name() == null){
            throw new ValidationException("Имя жанра не может быть пустым");
        }
        genreService.createGenre(new Genre(genreRequestDTO.name()));
        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id){
        if (!genreService.existsById(id)){
            return new ResponseEntity<>("Жанр не найден", HttpStatus.NOT_FOUND);
        }
        genreService.deleteGenre(id);
        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Genre genre = genreService.findById(id);
        if (genre == null){
            return new ResponseEntity<>("Жанр не найден", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GenreResponseDTO(genre.getId(), genre.getName()), HttpStatus.OK);
    }

    @GetMapping(params = "name")
    public ResponseEntity<?> findByName(@RequestParam String name){
        Genre genre = genreService.findByName(name);
        if (genre == null){
            return new ResponseEntity<>("Жанр не найден", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GenreResponseDTO(genre.getId(), genre.getName()), HttpStatus.OK);
    }

    @GetMapping("")
    public Page<GenreResponseDTO> findAll(@RequestParam int size, @RequestParam int page){
        return genreService.findAll(size, page).map(g -> new GenreResponseDTO(g.getId(), g.getName()));
    }

}
