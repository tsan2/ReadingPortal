package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.Volume;

import java.util.List;
import java.util.Optional;

public interface VolumeRepository extends JpaRepository<Volume, Long> {

    @Query("SELECT MAX(v.volumeMainNumber) FROM Volume v WHERE v.book.id = :bookId AND v.isDefault = false")
    Integer findLastMainNumberByBookId(@Param("bookId") Long bookId);

    List<Volume> findAllByBookIdAndIsDefaultFalse(Long bookId);
    Optional<Volume> findByBookIdAndIsDefaultTrue(Long bookId);
    int countByBookIdAndIsDefaultFalse(Long bookId);
    boolean existsByBookIdAndVolumeMainNumberAndVolumeSubNumberAndIsDefaultFalse(Long bookId,
                                                                                 int volumeMainNumber,
                                                                                 int volumeSubNumber);

    boolean existsByIdAndBookAuthorsId(Long volumeId, Long authorId);
}
