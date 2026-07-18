package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.*;
import ru.anastasya.readingportal.mappers.ChapterMapper;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.ChapterRepository;
import ru.anastasya.readingportal.repositories.VolumeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@AllArgsConstructor
@Service
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final VolumeRepository volumeRepository;
    private final BookRepository bookRepository;
    private final ChapterMapper chapterMapper;

    @PreAuthorize("@bookSecurity.canCreateChapterPlaceHolder(#bookId, #volumeId, principal.id)")
    @Transactional
    public ChapterShortResponseDTO createChapterPlaceHolder(ChapterCreateDTO dto,
                                                            Long bookId, Long volumeId){
        Volume volume = null;
        if (volumeId == null){
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Книга с таким айди не найдена"));
            volume = volumeRepository.findByBookIdAndIsDefaultTrue(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("У этой книги есть тома. Нельзя добавить главу напрямую к книге"));
            volumeId = volume.getId();
        } else {
            volume = volumeRepository.findById(volumeId).orElseThrow(()
                    -> new EntityNotFoundException("Том с таким айди не найден"));
        }

        Chapter chapter = new Chapter(dto.title(), dto.chapterMainNumber(), dto.chapterSubNumber());

        String warningMessage = null;

        if (chapterRepository.existsByVolumeIdAndChapterMainNumberAndChapterSubNumber(volumeId,
                chapter.getChapterMainNumber(), chapter.getChapterSubNumber())){
            throw new ConflictException("Такой номер главы уже существует");
        }

        Integer maxNumber = chapterRepository.findLastMainNumberByVolumeId(volumeId);
        if (maxNumber!=null && chapter.getChapterMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер главы. Последний номер сейчас: " + maxNumber;
        }

        chapter.setVolume(volume);
        Chapter chapterSaved = chapterRepository.saveAndFlush(chapter);

        ChapterShortResponseDTO chapterShortResponseDTO = chapterMapper.toChapterShortResponseDTO(chapterSaved);
        chapterShortResponseDTO.setVolumeId(volumeId);
        chapterShortResponseDTO.setWarningMessage(warningMessage);

        return chapterShortResponseDTO;
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByChapterId(#chapterId, principal.id)")
    @Transactional
    public ChapterFullDTO addContent(ChapterAddContentDTO chapterAddContentDTO, Long chapterId){
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException("Глава с таким айди не найдена"));
        if (!Objects.equals(chapterAddContentDTO.version(), chapter.getVersion())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }

        chapter.setContent(chapterAddContentDTO.content());

        ChapterFullDTO chapterFullDTO = chapterMapper.toChapterFullDTO(chapter);
        chapterFullDTO.setVolumeId(chapter.getVolume().getId());
        return chapterFullDTO;
    }


    @PreAuthorize("@bookSecurity.checkAuthorityByChapterId(#id, principal.id)")
    @Transactional
    public ChapterShortResponseDTO update(Long id, ChapterUpdateDTO dto){
        ChapterShortDTO chapter = chapterRepository.findShortById(id)
                .orElseThrow(() -> new EntityNotFoundException("Глава с таким айди не найдена"));

        if (!Objects.equals(dto.version(), chapter.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }

        ChapterShortResponseDTO chapterUpdated = chapterMapper.fromChapterShortDTOToResponse(chapter);

        if (dto.newTitle() != null){
            ChangeTitleDTO changeTitleDTO = new ChangeTitleDTO(dto.newTitle(), dto.version());
            chapterUpdated = chapterMapper.fromChapterShortDTOToResponse(changeTitle(changeTitleDTO, id));
        }

        if (dto.newChapterMainNumber() != null || dto.newChapterSubNumber() != null){
            ChangeChapterNumberDTO changeChapterNumberDTO = getChangeChapterNumberDTO(dto, chapter);

            chapterUpdated = changeChapterNumber(changeChapterNumberDTO, id);
        }
        return chapterUpdated;
    }

    private ChangeChapterNumberDTO getChangeChapterNumberDTO(ChapterUpdateDTO dto, ChapterShortDTO chapter) {
        Integer chapterMainNumber = null;
        Integer chapterSubNumber = null;
        if (dto.newChapterSubNumber() == null){
            chapterMainNumber = dto.newChapterMainNumber();
            chapterSubNumber = chapter.chapterSubNumber();
        }
        else if(dto.newChapterMainNumber() == null){
            chapterSubNumber = dto.newChapterSubNumber();
            chapterMainNumber = chapter.chapterMainNumber();
        }
        else{
            chapterSubNumber = dto.newChapterSubNumber();
            chapterMainNumber = dto.newChapterMainNumber();
        }
        ChangeChapterNumberDTO changeChapterNumberDTO = new ChangeChapterNumberDTO(
                chapterMainNumber,
                chapterSubNumber,
                dto.version());
        return changeChapterNumberDTO;
    }

    private ChapterShortDTO changeTitle(ChangeTitleDTO dto, Long id){

        chapterRepository.changeTitle(dto.newTitle(), id);

        return chapterRepository.findShortById(id)
                .orElseThrow(() -> new EntityNotFoundException("Глава с таким айди не найдена"));
    }

    private ChapterShortResponseDTO changeChapterNumber(ChangeChapterNumberDTO dto, Long id){
        String warningMessage = null;

        Long volumeId = chapterRepository.findVolumeIdByChapterId(id);

        //здесь если была одна глава с номером 2.4 по идее ее смена на 1.2 даст предупреждение
        if (chapterRepository.existsByVolumeIdAndChapterMainNumberAndChapterSubNumber(volumeId,
                dto.chapterMainNumber(), dto.chapterSubNumber())){
            throw new ConflictException("Такой номер главы уже существует");
        }
        int maxNumber = chapterRepository.findLastMainNumberByVolumeId(volumeId);
        if (dto.chapterMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер главы. Последний номер сейчас: " + maxNumber;
        }

        chapterRepository.changeChapterNumber(dto.chapterMainNumber(), dto.chapterSubNumber(), id);
        ChapterShortDTO chapterShortDTO = chapterRepository.findShortById(id)
                .orElseThrow(() -> new EntityNotFoundException("Глава с таким айди не найдена"));

        ChapterShortResponseDTO chapterShortResponseDTO = chapterMapper.fromChapterShortDTOToResponse(chapterShortDTO);
        chapterShortResponseDTO.setWarningMessage(warningMessage);
        chapterShortResponseDTO.setVolumeId(volumeId);

        return chapterShortResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<ChapterShortDTO> findAllShortByVolumeIdOrBookId(FindAllShortChapterDTO dto) {
        List<ChapterShortDTO> chapterShortDTOS = new ArrayList<>();
        if (dto.volumeId() == null){
            if (dto.bookId() == null){
                throw new ValidationException("Айди книги не может быть пустым");
            }
            Book book = bookRepository.findById(dto.bookId())
                    .orElseThrow(() -> new EntityNotFoundException("Книга с таким айди не найдена"));
            Volume volume = volumeRepository.findByBookIdAndIsDefaultTrue(dto.bookId())
                    .orElseThrow(() -> new ConflictException("У этой книги главы находятся внутри томов"));
            chapterShortDTOS = chapterRepository.findAllShortByVolumeId(volume.getId());
        }
        else{
            Volume volume = volumeRepository.findById(dto.volumeId())
                    .orElseThrow(() -> new EntityNotFoundException("Том с таким айди не найден"));
            chapterShortDTOS = chapterRepository.findAllShortByVolumeId(dto.volumeId());
        }
        return chapterShortDTOS;
    }

//    public ChapterShortDTO findShortById(Long id){
//        return chapterRepository.findShortById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Глава с таким айди не найдена"));
//    }

    @Transactional(readOnly = true)
    public ChapterFullDTO findFullById(Long id){
        return chapterRepository.findFullById(id)
                .orElseThrow(() -> new EntityNotFoundException("Глава с таким айди не найдена"));
    }

    @PreAuthorize("@bookSecurity.checkAuthorityByChapterId(#id, principal.id)")
    @Transactional
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }

//скорее всего не надо
//    public void deleteAllChapter(Long volumeId, Long currentUserId){
//        checkAuthorityByVolumeId(volumeId, currentUserId);
//        chapterDAO.deleteAllByVolumeId(volumeId);
//    }

}
