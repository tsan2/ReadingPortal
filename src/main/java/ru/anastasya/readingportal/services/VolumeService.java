package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.*;
import ru.anastasya.readingportal.mappers.VolumeMapper;
import ru.anastasya.readingportal.models.Book;
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
    private final VolumeMapper volumeMapper;


    @Transactional
    public VolumeResponseDTO createVolume(VolumeRequest volumeRequest, Long currentUserId, Long bookId){
        FractionalNumber number = mapToFractionalNumber(volumeRequest.volumeNumber());
        Volume volume = new Volume(volumeRequest.title(), number.mainNumber(), number.subNumber(), false);

        checkAuthorityByBookId(bookId, currentUserId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким айди не найдена"));

        String warningMessage = null;
        Long id = null;

        Integer maxNumber = volumeRepository.findLastMainNumberByBookId(bookId);
        if (maxNumber != null && volume.getVolumeMainNumber() > maxNumber + 1){
            warningMessage = "Вы пропускаете номер тома. Последний номер сейчас: " + maxNumber;
        }

        Volume finalVolume = null;
        if (volumeRepository.countByBookIdAndIsDefaultFalse(bookId)==0){
            Volume defaultVolume = findDefaultVolume(bookId);

            defaultVolume.setVolumeMainNumber(volume.getVolumeMainNumber());
            defaultVolume.setVolumeSubNumber(volume.getVolumeSubNumber());
            defaultVolume.setTitle(volume.getTitle());
            defaultVolume.setDefault(false);

            finalVolume = volumeRepository.saveAndFlush(defaultVolume);
        }
        else{
            if (volumeRepository.existsByBookIdAndVolumeMainNumberAndVolumeSubNumberAndIsDefaultFalse
                    (bookId,
                    volume.getVolumeMainNumber(),
                    volume.getVolumeSubNumber())){
                throw new ConflictException("Такой номер тома уже существует");
            }
            volume.setBook(book);
            finalVolume = volumeRepository.saveAndFlush(volume);
        }

        VolumeResponseDTO volumeResponseDTO = volumeMapper.toVolumeResponseDTO(finalVolume);
        volumeResponseDTO.setWarningMessage(warningMessage);
        return volumeResponseDTO;
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

    //поменять dto на одно общее и объединить
    @Transactional
    public VolumeResponseDTO updateVolume(UpdateVolumeDTO updateVolumeDTO, Long currentUserId, Long volumeId){
        checkAuthorityByVolumeId(volumeId, currentUserId);

        Volume volume = volumeRepository.findById(volumeId)
                .orElseThrow(() -> new EntityNotFoundException("Том с таким id не найден"));

        VolumeResponseDTO volumeResponseDTO = null;
        if (updateVolumeDTO.newTitle() != null){
            ChangeTitleDTO changeTitleDTO = new ChangeTitleDTO(updateVolumeDTO.newTitle(), updateVolumeDTO.version());
            volume = changeTitle(changeTitleDTO, volume);
            volumeResponseDTO = volumeMapper.toVolumeResponseDTO(volume);
        }
        if (updateVolumeDTO.volumeNumber() != null){
            ChangeVolumeNumberDTO changeVolumeNumberDTO = new ChangeVolumeNumberDTO(updateVolumeDTO.volumeNumber(),
                    updateVolumeDTO.version());
            volumeResponseDTO = changeVolumeNumber(changeVolumeNumberDTO, volume);
        }

        return volumeResponseDTO;
    }

    //надо сделать чтобы искался там сверху
    private Volume changeTitle(ChangeTitleDTO dto, Volume volume){
        if (!volume.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }
        volume.setTitle(dto.newTitle());

        return volume;
    }

    private VolumeResponseDTO changeVolumeNumber(ChangeVolumeNumberDTO dto, Volume volume){
        String warningMessage = null;

        if (!volume.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
        }

        FractionalNumber fractionalNumber = mapToFractionalNumber(dto.volumeNumber());
        int volumeMainNumber = fractionalNumber.mainNumber();
        int volumeSubNumber = fractionalNumber.subNumber();

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

        VolumeResponseDTO volumeResponseDTO = volumeMapper.toVolumeResponseDTO(volume);
        volumeResponseDTO.setWarningMessage(warningMessage);
        return volumeResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<VolumeSummaryDTO> findAllByBookId(Long bookId){
        List<Volume> volumes = volumeRepository.findAllByBookIdAndIsDefaultFalse(bookId);
        return volumeMapper.toVolumeSummaryDTOs(volumes);
    }

    @Transactional(readOnly = true)
    public VolumeResponseDTO findById(Long id){
        Volume volume = volumeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Том не найден"));
        return volumeMapper.toVolumeResponseDTO(volume);
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

    //вроде не нужно
//    @Transactional
//    public void deleteAllVolume(Long bookId, Long currentUserId){
//        checkAuthorityByBookId(bookId, currentUserId);
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new EntityNotFoundException("Книга с таким id не найдена"));
//        book.getVolumes().clear();
//        createDefaultVolume(bookId, currentUserId);
//    }
//
//    @Transactional
//    public void deleteDefaultVolume(Long bookId, Long currentUserId){
//        checkAuthorityByBookId(bookId, currentUserId);
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new EntityNotFoundException("Книга с таким id не найдена"));
//        Volume volume = volumeRepository.findByBookIdAndIsDefaultTrue(bookId);
//        if (volume.isDefault()){
//            book.removeVolume(volume);
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public boolean existsNotDefaultVolume(Long bookId){
//        return volumeRepository.countByBookIdAndIsDefaultFalse(bookId) > 0;
//    }

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
