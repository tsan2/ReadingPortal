package ru.anastasya.readingportal.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public void deleteGenre(Long id){
        genreRepository.deleteById(id);
    }

    public Genre findById(Long id){
        return genreRepository.findById(id).orElse(null);
    }

    public Genre findByName(String name){
        return genreRepository.findByName(name);
    }

    public Page<Genre> findAll(int size, int page){
        Pageable pageable = PageRequest.of(page-1, size);
        return genreRepository.findAll(pageable);
    }

    public boolean existsById(Long id){
        return genreRepository.existsById(id);
    }

//    public List<Genre> findAllByBookId(Long bookId){
//        return genreDAO.findAllByBookId(bookId);
//    }
}
