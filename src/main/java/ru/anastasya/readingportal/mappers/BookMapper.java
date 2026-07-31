package ru.anastasya.readingportal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.anastasya.readingportal.dto.BookResponseDTO;
import ru.anastasya.readingportal.dto.CreateBookDTO;
import ru.anastasya.readingportal.models.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    Book fromCreateBookDTO(CreateBookDTO bookDTO);
    BookResponseDTO toBookResponseDTO(Book book);
}
