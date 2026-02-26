package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.GenreDAO;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.Genre;

import java.util.List;
import java.util.Objects;

public class GenreService {

    private final GenreDAO genreDAO;

    public GenreService(GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    public void createGenre(Genre genre){
        Objects.requireNonNull(genre, "Нельзя создать null genre");
        if (genre.getName() == null || genre.getName().isBlank()){
            throw new ServiceException("Имя не может быть пустым");
        }
        if (genre.getName().length() > 100){
            throw new ServiceException("Имя слишком большое. Максимальная длина 100 символов");
        }

        genreDAO.save(genre);
    }

    public void deleteGenre(Long id){
        genreDAO.delete(id);
    }

    public Genre findById(Long id){
        return genreDAO.findById(id);
    }

    public Genre findByName(String name){
        return genreDAO.findByName(name);
    }

    public List<Genre> findAll(){
        return genreDAO.findAll();
    }
}
