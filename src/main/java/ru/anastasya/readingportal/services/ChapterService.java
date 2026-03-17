package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.BookDAO;
import ru.anastasya.readingportal.dao.ChapterDAO;
import ru.anastasya.readingportal.dto.ChapterCreateDTO;
import ru.anastasya.readingportal.exception.AuthorizationException;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.utils.OperationResult;

import java.util.List;
import java.util.Objects;

public class ChapterService {

    private final static ChapterService INSTANCE = new ChapterService();

    private ChapterService(){}

    public static ChapterService getInstance(){
        return INSTANCE;
    }

    private final ChapterDAO chapterDAO = ChapterDAO.getInstance();
    private final VolumeService volumeService = VolumeService.getInstance();
    private final BookDAO bookDAO = BookDAO.getInstance();


    // и возможно в подсчете глав добавить условие не дефолтная и в уникальном индексе
    public OperationResult createChapterPlaceHolder(ChapterCreateDTO dto, Long currentUserId){
        if (dto.getVolumeId() == null){
            Volume volume = volumeService.findDefaultVolume(dto.getBookId());
            dto.setVolumeId(volume.getId());
        }

        checkAuthorityByVolumeId(dto.getVolumeId(), currentUserId);

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

        Long id = chapterDAO.save(chapter);
        return new OperationResult(id, true, warningMessage);
    }

    public void addContent(Long chapterId, String content, Long currentUserId){
        checkAuthorityByChapterId(chapterId, currentUserId);
        if (content.length() > 2_000_000){
            throw new ServiceException("Текст главы слишком длинный. Максимум 2 миллиона символов");
        }

        chapterDAO.updateContent(chapterId, content);
    }

    public Chapter findFullChapter(Long id){
        return chapterDAO.findFullById(id);
    }

    public void changeTitle(Long id, String newTitle, Long currentUserId){
        checkAuthorityByChapterId(id, currentUserId);
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

    public OperationResult changeChapterNumber(Long id, int chapterMainNumber, int chapterSubNumber, Long currentUserId){
        checkAuthorityByChapterId(id, currentUserId);

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
        return new OperationResult(null, true, warningMessage);
    }

    public List<Chapter> findAllByVolumeId(Long volumeId){
        return chapterDAO.findAllInfoByVolumeId(volumeId);
    }

    public Chapter findInfoById(Long id){
        return chapterDAO.findInfoById(id);
    }

    public void deleteChapter(Long id, Long currentUserId){
        checkAuthorityByChapterId(id, currentUserId);
        chapterDAO.delete(id);
    }

    public void deleteAllChapter(Long volumeId, Long currentUserId){
        checkAuthorityByVolumeId(volumeId, currentUserId);
        chapterDAO.deleteAllByVolumeId(volumeId);
    }

    private void checkAuthorityByChapterId(Long chapterId, Long userId){
        Long volumeId = chapterDAO.findInfoById(chapterId).getVolumeId();
        Long bookId = volumeService.findById(volumeId).getBookId();

        if (!bookDAO.isUserAuthorOfBook(bookId, userId)){
            throw new AuthorizationException("У вас нет прав");
        }
    }

    private void checkAuthorityByVolumeId(Long volumeId, Long userId){
        Long bookId = volumeService.findById(volumeId).getBookId();

        if (!bookDAO.isUserAuthorOfBook(bookId, userId)){
            throw new AuthorizationException("У вас нет прав");
        }
    }
}
