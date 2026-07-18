package ru.anastasya.readingportal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.ConflictException;
import ru.anastasya.readingportal.exceptions.OptimisticLockException;
import ru.anastasya.readingportal.exceptions.ValidationException;
import ru.anastasya.readingportal.mappers.ChapterMapper;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.ChapterRepository;
import ru.anastasya.readingportal.repositories.VolumeRepository;
import ru.anastasya.readingportal.services.ChapterService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChapterServiceTest {
    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private VolumeRepository volumeRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ChapterMapper chapterMapper;
    @InjectMocks
    private ChapterService chapterService;

    private ChapterCreateDTO chapterCreateDTO;
    private Long volumeId;
    private Long bookId;
    private Long chapterId;
    private Volume volume;
    private Chapter chapterSaved;
    private ChapterShortResponseDTO chapterShortResponseDTO;
    private ChapterShortDTO chapterShortDTO;
    private Book book;
    private List<ChapterShortDTO> chapterShortDTOList;


    @BeforeEach
    void setUp(){
        chapterCreateDTO = new ChapterCreateDTO("title", 1, 5);
        volumeId = 1L;
        bookId = 1L;
        chapterId = 1L;
        volume = new Volume();
        volume.setId(1L);

        chapterSaved = new Chapter
                (chapterCreateDTO.title(), chapterCreateDTO.chapterMainNumber(), chapterCreateDTO.chapterSubNumber());
        chapterSaved.setId(1L);
        chapterSaved.setVolume(volume);
        chapterSaved.setVersion(0);

        chapterShortResponseDTO = new ChapterShortResponseDTO();
        chapterShortResponseDTO.setTitle(chapterSaved.getTitle());
        chapterShortResponseDTO.setChapterMainNumber(chapterSaved.getChapterMainNumber());
        chapterShortResponseDTO.setChapterSubNumber(chapterSaved.getChapterSubNumber());
        chapterShortResponseDTO.setId(chapterSaved.getId());

        book = new Book();

        chapterShortDTO = new ChapterShortDTO
                (1L, "title", 1, 5, 0);


        chapterShortDTOList = List.of(chapterShortDTO);
    }

    @Test
    void createChapterPlaceHolder_byBookId_success(){
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(volumeRepository.findByBookIdAndIsDefaultTrue(bookId)).thenReturn(Optional.of(volume));

        createChapterPlaceHolderSuccessStubbing();

        ChapterShortResponseDTO responseDTO = chapterService
                .createChapterPlaceHolder(chapterCreateDTO, 1L, null);

        createChapterPlaceHolderSuccessAssert(responseDTO);

    }

    @Test
    void createChapterPlaceHolder_byVolumeId_success(){
        when(volumeRepository.findById(volumeId)).thenReturn(Optional.of(volume));
        createChapterPlaceHolderSuccessStubbing();

        ChapterShortResponseDTO responseDTO = chapterService.createChapterPlaceHolder
                (chapterCreateDTO, 1L, volumeId);


        createChapterPlaceHolderSuccessAssert(responseDTO);
    }

    @Test
    void createChapterPlaceHolder_ConflictException(){
        when(volumeRepository.findById(volumeId)).thenReturn(Optional.of(volume));
        when(chapterRepository.existsByVolumeIdAndChapterMainNumberAndChapterSubNumber
                (volumeId, chapterCreateDTO.chapterMainNumber(), chapterCreateDTO.chapterSubNumber()))
                .thenReturn(true);

        assertThatThrownBy(() -> chapterService.createChapterPlaceHolder(chapterCreateDTO, 1L, volumeId))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void addContent_success() {
        ChapterAddContentDTO addContentDTO = new ChapterAddContentDTO("text", 0);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapterSaved));

        ChapterFullDTO chapterFullDTO = new ChapterFullDTO();
        chapterFullDTO.setId(chapterSaved.getId());
        chapterFullDTO.setContent(addContentDTO.content());

        when(chapterMapper.toChapterFullDTO(chapterSaved)).thenReturn(chapterFullDTO);

        ChapterFullDTO chapterFullDTOResponse = chapterService.addContent(addContentDTO, chapterId);

        assertThat(chapterFullDTOResponse.getId()).isEqualTo(1L);
        assertThat(chapterFullDTOResponse.getContent()).isEqualTo("text");
        assertThat(chapterFullDTOResponse.getVolumeId()).isEqualTo(1L);
        assertThat(chapterSaved.getContent()).isEqualTo("text");
    }

    @Test
    void addContent_OptimisticLockException(){
        ChapterAddContentDTO addContentDTO = new ChapterAddContentDTO("text", 1);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapterSaved));

        assertThatThrownBy(() -> chapterService.addContent(addContentDTO, chapterId))
                .isInstanceOf(OptimisticLockException.class);
    }

    @Test
    void updateTitle_success(){
        ChapterUpdateDTO chapterUpdateDTO = new ChapterUpdateDTO
                ("newTitle", 0, null, null);

        ChapterShortDTO chapterShortDTOUpdated = new ChapterShortDTO
                (1L, chapterUpdateDTO.newTitle(), 1, 5, 0);

        when(chapterRepository.findShortById(chapterId))
                .thenReturn(Optional.of(chapterShortDTO))
                .thenReturn(Optional.of(chapterShortDTOUpdated));
        when(chapterMapper.fromChapterShortDTOToResponse(chapterShortDTO)).thenReturn(chapterShortResponseDTO);

        ChapterShortResponseDTO chapterShortResponseDTOUpdated = new ChapterShortResponseDTO();
        chapterShortResponseDTOUpdated.setTitle(chapterUpdateDTO.newTitle());
        chapterShortResponseDTOUpdated.setId(chapterId);

        when(chapterMapper.fromChapterShortDTOToResponse(chapterShortDTOUpdated))
                .thenReturn(chapterShortResponseDTOUpdated);

        ChapterShortResponseDTO responseDTO = chapterService.update(chapterId, chapterUpdateDTO);

        verify(chapterRepository, times(1)).changeTitle(chapterUpdateDTO.newTitle(), chapterId);
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getTitle()).isEqualTo("newTitle");
    }

    @Test
    void updateNumber_success(){
        ChapterUpdateDTO chapterUpdateDTO = new ChapterUpdateDTO
                (null, 0, 0, 1);

        ChapterShortDTO chapterShortDTOUpdated = new ChapterShortDTO
                (1L, "title", chapterUpdateDTO.newChapterMainNumber(),
                        chapterUpdateDTO.newChapterSubNumber(), 0);

        when(chapterRepository.findShortById(chapterId))
                .thenReturn(Optional.of(chapterShortDTO))
                .thenReturn(Optional.of(chapterShortDTOUpdated));
        when(chapterMapper.fromChapterShortDTOToResponse(chapterShortDTO)).thenReturn(chapterShortResponseDTO);

        when(chapterRepository.findVolumeIdByChapterId(chapterId)).thenReturn(volumeId);
        when(chapterRepository.existsByVolumeIdAndChapterMainNumberAndChapterSubNumber(volumeId,
                chapterUpdateDTO.newChapterMainNumber(), chapterUpdateDTO.newChapterSubNumber()))
                .thenReturn(false);
        when(chapterRepository.findLastMainNumberByVolumeId(volumeId)).thenReturn(0);

        ChapterShortResponseDTO chapterShortResponseDTOUpdated = new ChapterShortResponseDTO();
        chapterShortResponseDTOUpdated.setId(chapterId);
        chapterShortResponseDTOUpdated.setChapterMainNumber(chapterUpdateDTO.newChapterMainNumber());
        chapterShortResponseDTOUpdated.setChapterSubNumber(chapterUpdateDTO.newChapterSubNumber());

        when(chapterMapper.fromChapterShortDTOToResponse(chapterShortDTOUpdated))
                .thenReturn(chapterShortResponseDTOUpdated);

        ChapterShortResponseDTO responseDTO = chapterService.update(chapterId, chapterUpdateDTO);

        verify(chapterRepository, times(1))
                .changeChapterNumber(chapterUpdateDTO.newChapterMainNumber(),
                        chapterUpdateDTO.newChapterSubNumber(), chapterId);

        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getChapterMainNumber()).isEqualTo(0);
        assertThat(responseDTO.getChapterSubNumber()).isEqualTo(1);
        assertThat(responseDTO.getWarningMessage()).isNull();
        assertThat(responseDTO.getVolumeId()).isEqualTo(volumeId);
    }

    @Test
    void update_optimisticLockException(){
        ChapterUpdateDTO chapterUpdateDTO = new ChapterUpdateDTO
                ("newTitle", 1, null, null);
        when(chapterRepository.findShortById(chapterId))
                .thenReturn(Optional.of(chapterShortDTO));

        assertThatThrownBy(() -> chapterService.update(chapterId, chapterUpdateDTO))
                .isInstanceOf(OptimisticLockException.class);
    }

    @Test
    void findAllShort_byVolumeId_success(){
        FindAllShortChapterDTO findAllShortChapterDTO = new FindAllShortChapterDTO(null, volumeId);

        when(volumeRepository.findById(volumeId)).thenReturn(Optional.of(volume));

        when(chapterRepository.findAllShortByVolumeId(volumeId)).thenReturn(chapterShortDTOList);

        List<ChapterShortDTO> result = chapterService.findAllShortByVolumeIdOrBookId(findAllShortChapterDTO);
        assertThat(result).hasSize(1)
                .contains(chapterShortDTO);
    }

    @Test
    void findAllShort_byBookId_success(){
        FindAllShortChapterDTO findAllShortChapterDTO = new FindAllShortChapterDTO(bookId, null);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(volumeRepository.findByBookIdAndIsDefaultTrue(bookId)).thenReturn(Optional.of(volume));
        when(chapterRepository.findAllShortByVolumeId(volumeId)).thenReturn(chapterShortDTOList);

        List<ChapterShortDTO> result = chapterService.findAllShortByVolumeIdOrBookId(findAllShortChapterDTO);
        assertThat(result).hasSize(1)
                .contains(chapterShortDTO);
    }

    @Test
    void findAllShort_byBookId_conflictException(){
        FindAllShortChapterDTO findAllShortChapterDTO = new FindAllShortChapterDTO(bookId, null);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(volumeRepository.findByBookIdAndIsDefaultTrue(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chapterService.findAllShortByVolumeIdOrBookId(findAllShortChapterDTO))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void findAllShort_validationException(){
        FindAllShortChapterDTO findAllShortChapterDTO = new FindAllShortChapterDTO(null, null);
        assertThatThrownBy(() -> chapterService.findAllShortByVolumeIdOrBookId(findAllShortChapterDTO))
                .isInstanceOf(ValidationException.class);
    }

    private void createChapterPlaceHolderSuccessStubbing(){
        when(chapterRepository.existsByVolumeIdAndChapterMainNumberAndChapterSubNumber
                (volumeId, chapterCreateDTO.chapterMainNumber(), chapterCreateDTO.chapterSubNumber()))
                .thenReturn(false);
        when(chapterRepository.findLastMainNumberByVolumeId(volumeId)).thenReturn(0);


        when(chapterRepository.saveAndFlush(any(Chapter.class))).thenReturn(chapterSaved);

        when(chapterMapper.toChapterShortResponseDTO(any(Chapter.class))).thenReturn(chapterShortResponseDTO);
    }

    private void createChapterPlaceHolderSuccessAssert(ChapterShortResponseDTO responseDTO){
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getVolumeId()).isEqualTo(1L);
        assertThat(responseDTO.getTitle()).isEqualTo("title");
        assertThat(responseDTO.getChapterMainNumber()).isEqualTo(1);
        assertThat(responseDTO.getChapterSubNumber()).isEqualTo(5);
    }
}
