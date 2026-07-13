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
import ru.anastasya.readingportal.mappers.GenreMapper;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.repositories.GenreRepository;

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
        if (!existsById(id)){
            throw new EntityNotFoundException("Жанр не найден");
        }
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public GenreResponseDTO findById(Long id){
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        return genreMapper.toGenreResponseDTO(genre);
    }

    @Transactional(readOnly = true)
    public GenreResponseDTO findByName(String name){
        Genre genre = genreRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Жанр не найден"));
        return genreMapper.toGenreResponseDTO(genre);
    }

    @Transactional(readOnly = true)
    public Page<GenreResponseDTO> findAll(int size, int page){
        Pageable pageable = PageRequest.of(page-1, size);
        return genreRepository.findAll(pageable).map(genreMapper :: toGenreResponseDTO);
    }

    @Transactional(readOnly = true)
    private boolean existsById(Long id){
        return genreRepository.existsById(id);
    }

//    public List<Genre> findAllByBookId(Long bookId){
//        return genreDAO.findAllByBookId(bookId);
//    }
}
