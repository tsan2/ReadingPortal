package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.ChapterDAO;
import ru.anastasya.readingportal.dao.VolumeDAO;
import ru.anastasya.readingportal.dto.ChapterCreateDTO;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.utils.OperationResult;

import java.util.List;
import java.util.Objects;

public class ChapterService {

    private final ChapterDAO chapterDAO;
    private final VolumeService volumeService;

    public ChapterService(ChapterDAO chapterDAO, VolumeService volumeService){
        this.chapterDAO = chapterDAO;
        this.volumeService = volumeService;
    }

    // и возможно в подсчете глав добавить условие не дефолтная и в уникальном индексе
    public OperationResult createChapter(ChapterCreateDTO dto){
        if (dto.getVolumeId() == null){
            Volume volume = volumeService.findDefaultVolume(dto.getBookId());
            dto.setVolumeId(volume.getId());
        }
        Chapter chapter = new Chapter(dto.getTitle(), dto.getChapterMainNumber(), dto.getChapterSubNumber(), dto.getVolumeId());

        Objects.requireNonNull(chapter.getVolumeId(), "volumeId не может быть null");

        String warningMessage = null;

        if (chapter.getTitle() == null || chapter.getTitle().isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (chapter.getTitle().length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }
        if (chapter.getChapterMainNumber() < 0 || chapter.getChapterSubNumber() < 0){
            throw new ServiceException("Номер главы не может быть меньше 0");
        }
        if (chapterDAO.existsChapterNumber(chapter.getVolumeId(), chapter.getChapterMainNumber(), chapter.getChapterSubNumber())){
            throw new ServiceException("Такой номер главы уже существует");
        }

        int maxNumber = chapterDAO.findLastMainNumberByVolumeId(chapter.getVolumeId());
        if (chapter.getChapterMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер главы. Последний номер сейчас: " + maxNumber;
        }

        chapterDAO.save(chapter);
        return new OperationResult(true, warningMessage);
    }

    public void addContent(Long chapterId, String content){
        if (content.length() > 2_000_000){
            throw new ServiceException("Текст главы слишком длинный. Максимум 2 миллиона символов");
        }

        chapterDAO.updateContent(chapterId, content);
    }

    public Chapter findFullChapter(Long id){
        return chapterDAO.findFullById(id);
    }

    public void changeTitle(Long id, String newTitle){
        Chapter chapter = chapterDAO.findInfoById(id);
        if (newTitle == null || newTitle.isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (chapter.getTitle().length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }
        chapter.setTitle(newTitle);

        chapterDAO.updateMetadata(chapter);
    }

    public OperationResult changeChapterNumber(Long id, int chapterMainNumber, int chapterSubNumber){
        Chapter chapter = chapterDAO.findInfoById(id);
        String warningMessage = null;

        if (chapter.getChapterMainNumber() < 0 || chapter.getChapterSubNumber() < 0){
            throw new ServiceException("Номер главы не может быть меньше 0");
        }
        if (chapterDAO.existsChapterNumber(chapter.getVolumeId(), chapter.getChapterMainNumber(), chapter.getChapterSubNumber())){
            throw new ServiceException("Такой номер главы уже существует");
        }
        int maxNumber = chapterDAO.findLastMainNumberByVolumeId(chapter.getVolumeId());
        if (chapter.getChapterMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер главы. Последний номер сейчас: " + maxNumber;
        }

        chapter.setChapterMainNumber(chapterMainNumber);
        chapter.setChapterSubNumber(chapterSubNumber);

        chapterDAO.updateMetadata(chapter);
        return new OperationResult(true, warningMessage);
    }

    public List<Chapter> findAllByVolumeId(Long volumeId){
        return chapterDAO.findAllInfoByVolumeId(volumeId);
    }

    public void deleteChapter(Long id){
        chapterDAO.delete(id);
    }

    public void deleteAllChapter(Long volumeId){
        chapterDAO.deleteAllByVolumeId(volumeId);
    }
}
