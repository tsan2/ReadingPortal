package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.GenreRequestDTO;
import ru.anastasya.readingportal.dto.GenreResponseDTO;
import ru.anastasya.readingportal.exceptions.EntityNotFoundException;
import ru.anastasya.readingportal.exceptions.ValidationException;
import ru.anastasya.readingportal.mappers.GenreMapper;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.repositories.GenreRepository;

import java.util.Objects;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public GenreResponseDTO createGenre(GenreRequestDTO genreRequestDTO){
        Genre genre = genreMapper.fromGenreRequestDTO(genreRequestDTO);
        Genre newGenre = genreRepository.save(genre);
        return genreMapper.toGenreResponseDTO(genre);
    }

    @Transactional
    public void deleteGenre(Long id){
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Genre findById(Long id){
        return genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name){
        return genreRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
    }

    @Transactional(readOnly = true)
    public Page<Genre> findAll(int size, int page){
        Pageable pageable = PageRequest.of(page-1, size);
        return genreRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id){
        return genreRepository.existsById(id);
    }

//    public List<Genre> findAllByBookId(Long bookId){
//        return genreDAO.findAllByBookId(bookId);
//    }
}
