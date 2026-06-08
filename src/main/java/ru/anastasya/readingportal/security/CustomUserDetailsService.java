package ru.anastasya.readingportal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.anastasya.readingportal.exceptions.EntityNotFoundException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrNickname(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return new CustomUserDetails(user);
    }
}
