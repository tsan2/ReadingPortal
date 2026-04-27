package ru.anastasya.readingportal.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.repositories.GenreRepository;

import java.util.List;
import java.util.Objects;

@Service
public class GenreService {

    GenreService(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    private final GenreRepository genreRepository;

    @Transactional
    public void createGenre(Genre genre){
        Objects.requireNonNull(genre, "Нельзя создать null genre");
        if (genre.getName() == null || genre.getName().isBlank()){
            throw new ValidationException("Имя не может быть пустым");
        }
        if (genre.getName().length() > 100){
            throw new ValidationException("Имя слишком большое. Максимальная длина 100 символов");
        }

        genreRepository.save(genre);
    }

    @Transactional
    public void deleteGenre(Long id){
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Genre findById(Long id){
        return genreRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name){
        return genreRepository.findByName(name);
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
