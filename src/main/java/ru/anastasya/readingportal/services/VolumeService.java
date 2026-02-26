package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.VolumeDAO;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.utils.OperationResult;

import java.util.List;
import java.util.Objects;

public class VolumeService {

    private final VolumeDAO volumeDAO;

    public VolumeService(VolumeDAO volumeDAO){
        this.volumeDAO = volumeDAO;
    }

    public OperationResult createVolume(Volume volume){
        Objects.requireNonNull(volume.getBookId(), "bookId не может быть null");

        String warningMessage = null;

        if (volume.getTitle() == null || volume.getTitle().isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (volume.getTitle().length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }
        if (volume.getVolumeMainNumber() < 0 || volume.getVolumeSubNumber() < 0){
            throw new ServiceException("Номер тома не может быть меньше 0");
        }
        int maxNumber = volumeDAO.findLastMainNumberByBookId(volume.getBookId());
        if (volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        volume.setIs_default(false);

        if (volumeDAO.getVolumeCountByBookId(volume.getBookId())==0){
            Volume defaultVolume = findDefaultVolume(volume.getBookId());
            volume.setId(defaultVolume.getId());
            volumeDAO.update(volume);
        }
        else{
            if (volumeDAO.existsVolumeNumber(volume.getBookId(), volume.getVolumeMainNumber(), volume.getVolumeSubNumber())){
                throw new ServiceException("Такой номер тома уже существует");
            }
            volumeDAO.save(volume);
        }
        return new OperationResult(true, warningMessage);
    }

    public Long createDefaultVolume(Long bookId){
        return volumeDAO.createDefaultByBookId(bookId);
    }
    //при удалении тома добавить логику если последний то создаем опять дефолтный
    public Volume findDefaultVolume(Long bookId){
        return volumeDAO.findDefaultByBookId(bookId);
    }

    public void changeTitle(Long id, String newTitle){
        Volume volume = volumeDAO.findById(id);
        if (newTitle == null || newTitle.isBlank()){
            throw new ServiceException("Название не может быть пустым");
        }
        if (volume.getTitle().length()>250){
            throw new ServiceException("Название не может быть длиннее 250 символов");
        }
        volume.setTitle(newTitle);

        volumeDAO.update(volume);
    }

    public OperationResult changeVolumeNumber(Long id, int volumeMainNumber, int volumeSubNumber){
        Volume volume = volumeDAO.findById(id);
        String warningMessage = null;

        if (volume.getVolumeMainNumber() < 0 || volume.getVolumeSubNumber() < 0){
            throw new ServiceException("Номер тома не может быть меньше 0");
        }
        if (volumeDAO.existsVolumeNumber(volume.getBookId(), volume.getVolumeMainNumber(), volume.getVolumeSubNumber())){
            throw new ServiceException("Такой номер тома уже существует");
        }
        int maxNumber = volumeDAO.findLastMainNumberByBookId(volume.getBookId());
        if (volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        volume.setVolumeMainNumber(volumeMainNumber);
        volume.setVolumeSubNumber(volumeSubNumber);

        volumeDAO.update(volume);
        return new OperationResult(true, warningMessage);
    }

    public List<Volume> findAllByBookId(Long book_id){
        return volumeDAO.findAllByBookId(book_id);
    }

    public void deleteVolume(Long id){
        volumeDAO.delete(id);
        if (volumeDAO.getVolumeCountByBookId(id) == 0){
            createDefaultVolume(id);
        }
    }

    public void deleteAllVolume(Long bookId){
        volumeDAO.deleteAllByBookId(bookId);
        createDefaultVolume(bookId);
    }

    public void deleteDefaultVolume(Long bookId){
        Volume volume = volumeDAO.findById(bookId);
        if (volume.isIs_default()){
            volumeDAO.delete(bookId);
        }
    }



}
