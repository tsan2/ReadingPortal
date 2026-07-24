package ru.anastasya.readingportal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.anastasya.readingportal.dto.ChapterFullDTO;
import ru.anastasya.readingportal.dto.ChapterShortDTO;
import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.models.Volume;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest
public class ChapterRepositoryTest {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private VolumeRepository volumeRepository;

    private Chapter chapter1;
    private Chapter chapter2;
    private Volume volume;

    @BeforeEach
    void setUp(){
        chapter1 = new Chapter();
        chapter1.setTitle("title1");
        chapter2 = new Chapter();
        chapter2.setTitle("title2");

        volume = new Volume();
        volume.setTitle("title");
    }

    @Test
    void findVolumeIdByChapterId_success(){
        volume.addChapter(chapter1);

        Volume savedVolume = volumeRepository.saveAndFlush(volume);
        Long volumeId = chapterRepository.findVolumeIdByChapterId(chapter1.getId());
        assertThat(volumeId).isEqualTo(savedVolume.getId());
    }

    @Test
    void changeTitle_success(){
        Chapter savedChapter = chapterRepository.saveAndFlush(chapter1);
        chapterRepository.changeTitle("newTitle", savedChapter.getId());

        Chapter updatedChapter = chapterRepository.findById(savedChapter.getId()).orElse(null);
        assertThat(updatedChapter.getTitle()).isEqualTo("newTitle");
    }

    @Test
    void changeChapterNumber_success(){
        chapter1.setChapterMainNumber(1);
        chapter1.setChapterSubNumber(0);

        Chapter savedChapter = chapterRepository.saveAndFlush(chapter1);
        chapterRepository.changeChapterNumber(2, 1, savedChapter.getId());

        Chapter updatedChapter = chapterRepository.findById(savedChapter.getId()).orElse(null);
        assertThat(updatedChapter.getChapterMainNumber()).isEqualTo(2);
        assertThat(updatedChapter.getChapterSubNumber()).isEqualTo(1);
    }

    @Test
    void findLastMainNumberByVolumeId_success(){
        chapter1.setChapterMainNumber(1);
        chapter2.setChapterMainNumber(2);

        volume.addChapter(chapter1);
        volume.addChapter(chapter2);
        Volume savedVolume = volumeRepository.saveAndFlush(volume);

        Integer lastMainNumber = chapterRepository.findLastMainNumberByVolumeId(savedVolume.getId());
        assertThat(lastMainNumber).isEqualTo(2);
    }

    @Test
    void findAllShortByVolumeId_success(){
        volume.addChapter(chapter1);
        volume.addChapter(chapter2);
        Volume savedVolume = volumeRepository.saveAndFlush(volume);

        List<ChapterShortDTO> chapterShortDTOS = chapterRepository.findAllShortByVolumeId(savedVolume.getId());
        assertThat(chapterShortDTOS)
                .hasSize(2)
                .extracting(ChapterShortDTO::title)
                .containsExactlyInAnyOrder("title1", "title2");
    }

    @Test
    void findShortById_success(){
        Chapter savedChapter = chapterRepository.saveAndFlush(chapter1);
        ChapterShortDTO chapterShortDTO = chapterRepository.findShortById(savedChapter.getId()).orElse(null);

        assertThat(chapterShortDTO.title()).isEqualTo("title1");
    }

    @Test
    void findFullById_success(){
        Chapter savedChapter = chapterRepository.saveAndFlush(chapter1);
        ChapterFullDTO chapterFullDTO = chapterRepository.findFullById(savedChapter.getId()).orElse(null);

        assertThat(chapterFullDTO.getTitle()).isEqualTo("title1");
    }

}
