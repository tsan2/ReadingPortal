package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.dto.ChapterFullDTO;
import ru.anastasya.readingportal.dto.ChapterShortDTO;
import ru.anastasya.readingportal.models.Chapter;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    @Query("SELECT c.volume.id FROM Chapter c WHERE c.id=:chapterId")
    Long findVolumeIdByChapterId(@Param("chapterId") Long chapterId);

    @Query("SELECT MAX(c.chapterMainNumber) FROM Chapter c WHERE c.volume.id = :volumeId")
    Integer findLastMainNumberByVolumeId(@Param("volumeId") Long volumeId);

    @Query("""
        SELECT new ru.anastasya.readingportal.dto.ChapterShortDTO(c.id, c.title,
                c.chapterMainNumber, c.chapterSubNumber, c.version)
        FROM Chapter c WHERE c.volume.id=:volumeId""")
    List<ChapterShortDTO> findAllShortByVolumeId(@Param("volumeId") Long volumeId);

    @Query("""
        SELECT new ru.anastasya.readingportal.dto.ChapterShortDTO(c.id, c.title,
                c.chapterMainNumber, c.chapterSubNumber, c.version)
        FROM Chapter c WHERE c.id=:id""")
    Optional<ChapterShortDTO> findShortById(@Param("id") Long id);

    @Query("""
        SELECT new ru.anastasya.readingportal.dto.ChapterFullDTO(c.id, c.title, c.content,
                c.chapterMainNumber, c.chapterSubNumber, c.version, c.volume.id)
        FROM Chapter c WHERE c.id=:id""")
    Optional<ChapterFullDTO> findFullById(@Param("id") Long id);
    boolean existsByVolumeIdAndChapterMainNumberAndChapterSubNumber(Long volumeId,
                                                                    int chapterMainNumber, int chapterSubNumber);

    @Modifying
    @Query("UPDATE Chapter c SET c.title=:title, c.version=c.version+1 WHERE c.id=:id")
    void changeTitle(@Param("title") String title, @Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE Chapter c
        SET c.chapterMainNumber=:chapterMainNumber,
        c.chapterSubNumber=:chapterSubNumber,
        c.version=c.version+1
        WHERE c.id=:id""")
    void changeChapterNumber(@Param("chapterMainNumber") int chapterMainNumber,
                             @Param("chapterSubNumber") int chapterSubNumber,
                             @Param("id") Long id);

    boolean existsByIdAndVolumeBookAuthorsId(Long chapterId, Long authorId);
}
