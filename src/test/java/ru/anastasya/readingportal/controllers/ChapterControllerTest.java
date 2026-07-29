package ru.anastasya.readingportal.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.services.ChapterService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChapterControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ChapterService chapterService;


    @Test
    void createChapterPlaceholderInVolume_success() throws Exception {
        Long volumeId = 1L;
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("title", 1, 2);

        ChapterShortResponseDTO chapterShortResponseDTO = new ChapterShortResponseDTO();
        chapterShortResponseDTO.setTitle("title");
        chapterShortResponseDTO.setChapterMainNumber(1);
        chapterShortResponseDTO.setChapterSubNumber(2);

        when(chapterService.createChapterPlaceHolder(any(), eq(null), eq(volumeId)))
                .thenReturn(chapterShortResponseDTO);

        mockMvc.perform(post("/volume/1/chapter")
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chapterCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"));


    }

    @Test
    void createChapterPlaceholderInVolume_badRequest() throws Exception{
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("t", 1, 2);

        mockMvc.perform(post("/volume/1/chapter")
                        .with(user("testUser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterCreateDTO)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void createChapterPlaceholderInVolume_unauthorized() throws Exception{
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("title", 1, 2);

        mockMvc.perform(post("/volume/1/chapter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterCreateDTO)))
                        .andExpect(status().isUnauthorized())
                        .andDo(print());
    }

    @Test
    void createChapterPlaceholderInBook_success() throws Exception {
        Long bookId = 1L;
        ChapterCreateDTO chapterCreateDTO = new ChapterCreateDTO("title", 1, 2);

        ChapterShortResponseDTO chapterShortResponseDTO = new ChapterShortResponseDTO();
        chapterShortResponseDTO.setTitle("title");
        chapterShortResponseDTO.setChapterMainNumber(1);
        chapterShortResponseDTO.setChapterSubNumber(2);

        when(chapterService.createChapterPlaceHolder(any(), eq(bookId), eq(null)))
                .thenReturn(chapterShortResponseDTO);

        mockMvc.perform(post("/book/1/chapter")
                        .with(user("testUser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"));


    }

    @Test
    void addContent_success() throws Exception {
        Long id = 1L;
        ChapterAddContentDTO chapterAddContentDTO = new ChapterAddContentDTO("text", 0);

        ChapterFullDTO chapterFullDTO = new ChapterFullDTO();
        chapterFullDTO.setId(id);
        chapterFullDTO.setContent("text");
        when(chapterService.addContent(any(), eq(id))).thenReturn(chapterFullDTO);

        mockMvc.perform(put("/chapter/1/content")
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chapterAddContentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("text"));
    }

    @Test
    void update_success() throws Exception {
        ChapterUpdateDTO chapterUpdateDTO
                = new ChapterUpdateDTO("newTitle", 0,
                null, null);

        ChapterShortResponseDTO responseDTO = new ChapterShortResponseDTO();
        responseDTO.setTitle("newTitle");

        when(chapterService.update(1L, chapterUpdateDTO)).thenReturn(responseDTO);

        mockMvc.perform(patch("/chapter/1")
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chapterUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("newTitle"));
    }

    @Test
    void findAllShortInBook_success() throws Exception {
        findAllShort();

        mockMvc.perform(get("/book/1/chapter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @Test
    void findAllShortInVolume_success() throws Exception {
        findAllShort();

        mockMvc.perform(get("/volume/1/chapter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @Test
    void findById_success() throws Exception {
        ChapterFullDTO chapterFullDTO = new ChapterFullDTO();
        chapterFullDTO.setId(1L);

        when(chapterService.findFullById(1L)).thenReturn(chapterFullDTO);
        mockMvc.perform(get("/chapter/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    void deleteChapter_success() throws Exception {
        mockMvc.perform(delete("/chapter/1")
                .with(user("testUser").roles("USER")))
                .andExpect(status().isNoContent());
    }

    private void findAllShort() {
        ChapterShortDTO chapterShortDTO = new ChapterShortDTO(1L, "title", 1, 1, 0);

        when(chapterService.findAllShortByVolumeIdOrBookId(any())).thenReturn(List.of(chapterShortDTO));

    }
}
