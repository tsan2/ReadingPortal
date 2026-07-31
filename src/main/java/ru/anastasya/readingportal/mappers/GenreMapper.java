package ru.anastasya.readingportal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.anastasya.readingportal.dto.GenreRequestDTO;
import ru.anastasya.readingportal.dto.GenreResponseDTO;
import ru.anastasya.readingportal.models.Genre;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {

    Genre fromGenreRequestDTO(GenreRequestDTO genreRequestDTO);
    GenreResponseDTO toGenreResponseDTO(Genre genre);

}
