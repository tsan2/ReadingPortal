package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.Volume;

import java.util.List;

public interface VolumeRepository extends JpaRepository<Volume, Long> {

    @Query("SELECT MAX(v.volumeMainNumber) FROM Volume v WHERE v.book.id = :bookId AND v.isDefault = false")
    int findLastMainNumberByBookId(@Param("bookId") Long bookId);

    List<Volume> findAllByBookIdAndIsDefaultFalse(Long bookId);
    Volume findByBookIdAndIsDefaultTrue(Long bookId);
    int countByBookIdAndIsDefaultFalse(Long bookId);
    boolean existsByBookIdAndVolumeMainNumberAndVolumeSubNumberAndIsDefaultFalse(Long bookId, int volumeMainNumber, int volumeSubNumber);
}
