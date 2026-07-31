package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.PasswordResetCode;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    @Query("SELECT prc FROM PasswordResetCode prc WHERE prc.user.id = :idUser AND prc.code = :code AND prc.expiresAt > CURRENT_TIMESTAMP")
    PasswordResetCode findValidCode(@Param("idUser") Long idUser, @Param("code") String code);

    void deleteByUserId(Long idUser);

}
