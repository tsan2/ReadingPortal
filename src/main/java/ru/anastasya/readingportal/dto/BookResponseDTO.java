package ru.anastasya.readingportal.dto;

//import ru.anastasya.readingportal.models.Genre;

import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record BookResponseDTO(Long id,
                              String title,
                              LocalDateTime dateChanged,
                              LocalDateTime createdAt,
                              Integer version,
                              Set<GenreResponseDTO> genres,
                              Set<UserSummaryDTO> authors) {
}
