package ru.anastasya.readingportal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.anastasya.readingportal.dto.ChapterFullDTO;
import ru.anastasya.readingportal.dto.ChapterShortDTO;
import ru.anastasya.readingportal.dto.ChapterShortResponseDTO;
import ru.anastasya.readingportal.models.Chapter;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChapterMapper {

    ChapterShortResponseDTO toChapterShortResponseDTO(Chapter chapter);
    ChapterFullDTO toChapterFullDTO(Chapter chapter);
    ChapterShortResponseDTO fromChapterShortDTOToResponse(ChapterShortDTO chapterShortDTO);
}
