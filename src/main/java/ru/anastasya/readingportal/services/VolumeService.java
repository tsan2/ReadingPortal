package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.BookDAO;
import ru.anastasya.readingportal.dao.VolumeDAO;
import ru.anastasya.readingportal.exception.AuthenticationException;
import ru.anastasya.readingportal.exception.ConflictException;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.utils.OperationResult;

import java.util.List;
import java.util.Objects;

public class VolumeService {

    private final static VolumeService INSTANCE = new VolumeService();

    private VolumeService(){}

    public static VolumeService getInstance(){
        return INSTANCE;
    }

    private final VolumeDAO volumeDAO = VolumeDAO.getInstance();
    private final BookDAO bookDAO = BookDAO.getInstance();

    public OperationResult createVolume(Volume volume, Long currentUserId){
        Objects.requireNonNull(volume.getBookId(), "bookId не может быть null");

        checkAuthorityByBookId(volume.getBookId(), currentUserId);

        String warningMessage = null;
        Long id = null;

        if (volume.getTitle() == null || volume.getTitle().isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if (volume.getTitle().length()>250){
            throw new ValidationException("Название не может быть длиннее 250 символов");
        }
        if (volume.getVolumeMainNumber() < 0 || volume.getVolumeSubNumber() < 0){
            throw new ValidationException("Номер тома не может быть меньше 0");
        }
        int maxNumber = volumeDAO.findLastMainNumberByBookId(volume.getBookId());
        if (volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        volume.setDefault(false);

        if (volumeDAO.getNotDefaultVolumeCountByBookId(volume.getBookId())==0){
            Volume defaultVolume = findDefaultVolume(volume.getBookId());
            volume.setId(defaultVolume.getId());
            volumeDAO.update(volume);
        }
        else{
            if (volumeDAO.existsVolumeNumber(volume.getBookId(), volume.getVolumeMainNumber(), volume.getVolumeSubNumber())){
                throw new ConflictException("Такой номер тома уже существует");
            }
            id = volumeDAO.save(volume);
        }
        return new OperationResult(id, true, warningMessage);
    }

    public Long createDefaultVolume(Long bookId, Long currentUserId){
        checkAuthorityByBookId(bookId, currentUserId);
        return volumeDAO.createDefaultByBookId(bookId);
    }

    public Volume findDefaultVolume(Long bookId){
        return volumeDAO.findDefaultByBookId(bookId);
    }

    public void changeTitle(Long id, String newTitle, Long currentUserId){
        checkAuthorityByVolumeId(id, currentUserId);

        Volume volume = volumeDAO.findById(id);
        if (newTitle == null || newTitle.isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if (volume.getTitle().length()>250){
            throw new ValidationException("Название не может быть длиннее 250 символов");
        }
        volume.setTitle(newTitle);

        volumeDAO.update(volume);
    }

    public OperationResult changeVolumeNumber(Long id, int volumeMainNumber, int volumeSubNumber, Long currentUserId){
        checkAuthorityByVolumeId(id, currentUserId);

        Volume volume = volumeDAO.findById(id);
        String warningMessage = null;

        if (volume.getVolumeMainNumber() < 0 || volume.getVolumeSubNumber() < 0){
            throw new ValidationException("Номер тома не может быть меньше 0");
        }
        if (volumeDAO.existsVolumeNumber(volume.getBookId(), volume.getVolumeMainNumber(), volume.getVolumeSubNumber())){
            throw new ValidationException("Такой номер тома уже существует");
        }
        int maxNumber = volumeDAO.findLastMainNumberByBookId(volume.getBookId());
        if (volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        volume.setVolumeMainNumber(volumeMainNumber);
        volume.setVolumeSubNumber(volumeSubNumber);

        volumeDAO.update(volume);
        return new OperationResult(null, true, warningMessage);
    }

    public List<Volume> findAllByBookId(Long book_id){
        return volumeDAO.findNotDefaultAllByBookId(book_id);
    }

    public Volume findById(Long id){
        return volumeDAO.findById(id);
    }

    public void deleteVolume(Long id, Long currentUserId){
        checkAuthorityByVolumeId(id, currentUserId);

        volumeDAO.delete(id);
        if (volumeDAO.getNotDefaultVolumeCountByBookId(id) == 0){
            createDefaultVolume(id, currentUserId);
        }
    }

    public void deleteAllVolume(Long bookId, Long currentUserId){
        checkAuthorityByBookId(bookId, currentUserId);
        volumeDAO.deleteAllByBookId(bookId);
        createDefaultVolume(bookId, currentUserId);
    }

    public void deleteDefaultVolume(Long bookId, Long currentUserId){
        checkAuthorityByBookId(bookId, currentUserId);
        Volume volume = volumeDAO.findById(bookId);
        if (volume.isDefault()){
            volumeDAO.delete(bookId);
        }
    }

    public boolean existsNotDefaultVolume(Long bookId){
        return volumeDAO.getNotDefaultVolumeCountByBookId(bookId) > 0;
    }

    private void checkAuthorityByVolumeId(Long volumeId, Long userId){
        Long bookId = volumeDAO.findById(volumeId).getBookId();

        if (!bookDAO.isUserAuthorOfBook(bookId, userId)){
            throw new AuthenticationException("У вас нет прав");
        }
    }

    private void checkAuthorityByBookId(Long bookId, Long userId){
        if (!bookDAO.isUserAuthorOfBook(bookId, userId)){
            throw new AuthenticationException("У вас нет прав");
        }
    }

}
