package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.ChangeVolumeNumberDTO;
import ru.anastasya.readingportal.dto.ChangeVolumeTitleDTO;
import ru.anastasya.readingportal.dto.FractionalNumber;
import ru.anastasya.readingportal.dto.VolumeRequest;
import ru.anastasya.readingportal.exception.*;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.VolumeRepository;
import ru.anastasya.readingportal.utils.OperationResult;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class VolumeService {

    private final VolumeRepository volumeRepository;
    private final BookRepository bookRepository;

    @Transactional
    public OperationResult createVolume(Long bookId, VolumeRequest volumeRequest, Long currentUserId){
        FractionalNumber number = mapToFractionalNumber(volumeRequest.volumeNumber());
        Volume volume = new Volume(volumeRequest.title(), number.mainNumber(), number.subNumber(), false);

        checkAuthorityByBookId(bookId, currentUserId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким айди не найдена"));

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
        int maxNumber = volumeRepository.findLastMainNumberByBookId(bookId);
        if (volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        if (volumeRepository.countByBookIdAndIsDefaultFalse(bookId)==0){
            Volume defaultVolume = findDefaultVolume(bookId);

            defaultVolume.setVolumeMainNumber(volume.getVolumeMainNumber());
            defaultVolume.setVolumeSubNumber(volume.getVolumeSubNumber());
            defaultVolume.setTitle(volume.getTitle());
            defaultVolume.setDefault(false);

            book.addVolume(defaultVolume);
            bookRepository.save(book);
            id = defaultVolume.getId();
        }
        else{
            if (volumeRepository.existsByBookIdAndVolumeMainNumberAndVolumeSubNumberAndIsDefaultFalse
                    (bookId,
                    volume.getVolumeMainNumber(),
                    volume.getVolumeSubNumber())){
                throw new ConflictException("Такой номер тома уже существует");
            }
            book.addVolume(volume);
            bookRepository.save(book);
            id = volume.getId();
        }
        return new OperationResult(id, true, warningMessage);
    }

    @Transactional
    public Long createDefaultVolume(Long bookId, Long currentUserId){
        checkAuthorityByBookId(bookId, currentUserId);
        Volume volume = new Volume("Базовый том", 0, 0, true);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким айди не найдена"));
        book.addVolume(volume);
        bookRepository.save(book);
        return volume.getId();
    }

    @Transactional(readOnly = true)
    public Volume findDefaultVolume(Long bookId){
        return volumeRepository.findByBookIdAndIsDefaultTrue(bookId);
    }

    @Transactional
    public void changeTitle(ChangeVolumeTitleDTO dto){
        checkAuthorityByVolumeId(dto.id(), dto.currentUserId());

        Volume volume = volumeRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Том с таким id не найден"));
        if (!volume.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        if (dto.newTitle() == null || dto.newTitle().isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if (volume.getTitle().length()>250){
            throw new ValidationException("Название не может быть длиннее 250 символов");
        }
        volume.setTitle(dto.newTitle());
    }

    @Transactional
    public OperationResult changeVolumeNumber(ChangeVolumeNumberDTO dto){
        checkAuthorityByVolumeId(dto.id(), dto.currentUserId());

        Volume volume = volumeRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Том с таким id не найден"));
        String warningMessage = null;

        if (!volume.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }

        FractionalNumber fractionalNumber = mapToFractionalNumber(dto.volumeNumber());
        int volumeMainNumber = fractionalNumber.mainNumber();
        int volumeSubNumber = fractionalNumber.subNumber();

        if (volumeMainNumber < 0 || volumeSubNumber < 0){
            throw new ValidationException("Номер тома не может быть меньше 0");
        }
        if (volumeRepository.existsByBookIdAndVolumeMainNumberAndVolumeSubNumberAndIsDefaultFalse
                (volume.getBook().getId(), volumeMainNumber, volumeSubNumber)){
            throw new ValidationException("Такой номер тома уже существует");
        }

        int maxNumber = volumeRepository.findLastMainNumberByBookId(volume.getBook().getId());
        if (volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        volume.setVolumeMainNumber(volumeMainNumber);
        volume.setVolumeSubNumber(volumeSubNumber);

        return new OperationResult(null, true, warningMessage);
    }

    @Transactional(readOnly = true)
    public List<Volume> findAllByBookId(Long book_id){
        return volumeRepository.findAllByBookIdAndIsDefaultFalse(book_id);
    }

    @Transactional(readOnly = true)
    public Volume findById(Long id){
        return volumeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteVolume(Long id, Long currentUserId){
        checkAuthorityByVolumeId(id, currentUserId);

        Volume volume = volumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Том с таким id не найден"));
        Book book = volume.getBook();
        book.removeVolume(volume);
        if (book.getVolumes().stream().noneMatch(v -> !v.isDefault())){
            createDefaultVolume(book.getId(), currentUserId);
        }
    }

    @Transactional
    public void deleteAllVolume(Long bookId, Long currentUserId){
        checkAuthorityByBookId(bookId, currentUserId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким id не найдена"));
        book.getVolumes().clear();
        createDefaultVolume(bookId, currentUserId);
    }

    @Transactional
    public void deleteDefaultVolume(Long bookId, Long currentUserId){
        checkAuthorityByBookId(bookId, currentUserId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким id не найдена"));
        Volume volume = volumeRepository.findByBookIdAndIsDefaultTrue(bookId);
        if (volume.isDefault()){
            book.removeVolume(volume);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsNotDefaultVolume(Long bookId){
        return volumeRepository.countByBookIdAndIsDefaultFalse(bookId) > 0;
    }

    private void checkAuthorityByVolumeId(Long volumeId, Long userId){
        Volume volume = volumeRepository.findById(volumeId)
                .orElseThrow(() -> new EntityNotFoundException("Том с таким id не найден"));
        Book book = volume.getBook();

        if (book.getAuthors().stream().noneMatch(user -> user.getId().equals(userId))){
            throw new ForbiddenException("У вас нет прав");
        }
    }

    private void checkAuthorityByBookId(Long bookId, Long userId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким id не найдена"));

        if (book.getAuthors().stream().noneMatch(user -> user.getId().equals(userId))){
            throw new ForbiddenException("У вас нет прав");
        }
    }

    private static FractionalNumber mapToFractionalNumber(double number){
        BigDecimal bigDecimal = BigDecimal.valueOf(number);
        if (bigDecimal.scale()>1){
            throw new ValidationException("Максимальное количество цифр после запятой - 1");
        }
        int mainNumber = (int) number;
        int subNumber = (int) Math.round((number - mainNumber) * 10);
        return new FractionalNumber(mainNumber, subNumber);
    }
}
