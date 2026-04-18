package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :emailOrNickname OR u.nickname = :emailOrNickname")
    User findByEmailOrNickname(@Param("emailOrNickname") String emailOrNickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
