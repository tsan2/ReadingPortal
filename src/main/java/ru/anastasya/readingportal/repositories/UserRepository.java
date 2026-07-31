package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :emailOrNickname OR u.nickname = :emailOrNickname")
    Optional<User> findByEmailOrNickname(@Param("emailOrNickname") String emailOrNickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
