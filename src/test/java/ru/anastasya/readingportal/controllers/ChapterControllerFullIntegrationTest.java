package ru.anastasya.readingportal.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.anastasya.readingportal.dto.ChapterCreateDTO;
import ru.anastasya.readingportal.dto.ChapterShortResponseDTO;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Role;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.ChapterRepository;
import ru.anastasya.readingportal.repositories.UserRepository;
import ru.anastasya.readingportal.repositories.VolumeRepository;
import ru.anastasya.readingportal.security.JwtProvider;


import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ChapterControllerFullIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");
    @Autowired
    private ChapterController chapterController;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VolumeRepository volumeRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(){
        this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port + "/reading-portal").build();
    }

    @AfterEach
    void cleanUp(){
        chapterRepository.deleteAll();
        volumeRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createChapterPlaceholderInBook_success(){
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("titleChapter", 1, 1);
        User user = new User("testUser", "testUser@example.com", "testPasswordHash");
        user.setRoles(Set.of(Role.USER));
        Book book = new Book("titleBook");
        Volume volume = new Volume("defaultVolume", 1, 1, true);
        book.addVolume(volume);
        book.addAuthor(user);

        User userSaved = userRepository.saveAndFlush(user);
        Book bookSaved = bookRepository.saveAndFlush(book);

        String accessToken = jwtProvider.generateAccessToken(userSaved);

        webTestClient.post()
                .uri("/book/{bookId}/chapter", bookSaved.getId())
                .headers(header -> header.setBearerAuth(accessToken))
                .bodyValue(chapterCreateDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ChapterShortResponseDTO.class)
                .value(response -> {
                    assertThat(response.getTitle()).isEqualTo("titleChapter");
                    assertThat(response.getVolumeId()).isEqualTo(bookSaved.getVolumes().get(0).getId());
                });


    }

    @Test
    void createChapterPlaceholderInBook_forbidden(){
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("titleChapter", 1, 1);
        User userAuthor = new User("testUserAuthor",
                "testUserAuthor@example.com",
                "testPasswordHash");
        userAuthor.setRoles(Set.of(Role.USER));
        Book book = new Book("titleBook");
        Volume volume = new Volume("defaultVolume", 1, 1, true);
        book.addVolume(volume);
        book.addAuthor(userAuthor);

        userRepository.saveAndFlush(userAuthor);
        Book bookSaved = bookRepository.saveAndFlush(book);

        User user = new User("testUser", "testUser@example.com", "testPasswordHash");
        user.setRoles(Set.of(Role.USER));

        User userSaved = userRepository.saveAndFlush(user);

        String accessToken = jwtProvider.generateAccessToken(userSaved);

        webTestClient.post()
                .uri("/book/{bookId}/chapter", bookSaved.getId())
                .headers(header -> header.setBearerAuth(accessToken))
                .bodyValue(chapterCreateDTO)
                .exchange()
                .expectStatus().isForbidden();


    }

    @Test
    void createChapterPlaceholderInBook_unauthorized(){
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("titleChapter", 1, 1);

        webTestClient.post()
                .uri("/book/{bookId}/chapter", 1L)
                .bodyValue(chapterCreateDTO)
                .exchange()
                .expectStatus().isUnauthorized();


    }
}
