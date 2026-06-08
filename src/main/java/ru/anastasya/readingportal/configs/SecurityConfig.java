package ru.anastasya.readingportal.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.models.Role;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.UserRepository;

import java.util.Set;

@Configuration
public class SecurityConfig {

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${app.admin.email}")
    private String ADMIN_EMAIL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/register", "/auth/forgot-password", "/auth/reset-password").permitAll()
                                .requestMatchers("/user/profile").authenticated()
                                .requestMatchers(HttpMethod.GET, "/book", "/genre", "/volume", "/user").permitAll()
                                .requestMatchers(HttpMethod.GET, "/book/**", "/genre/*", "/volume/**", "/user/*").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/genre").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/genre/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if (!userRepository.existsByNickname("admin")){
                String passwordHash = passwordEncoder.encode(ADMIN_PASSWORD);
                User user = new User("admin", ADMIN_EMAIL, passwordHash);
                user.setRoles(Set.of(Role.USER, Role.ADMIN));
                userRepository.save(user);
            }
        };
    }
}
