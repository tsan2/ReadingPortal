package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.anastasya.readingportal.models.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
