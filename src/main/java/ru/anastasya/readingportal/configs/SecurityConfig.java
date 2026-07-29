package ru.anastasya.readingportal.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.anastasya.readingportal.models.Role;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.UserRepository;
import ru.anastasya.readingportal.security.CustomAccessDeniedHandler;
import ru.anastasya.readingportal.security.CustomAuthenticationEntryPoint;
import ru.anastasya.readingportal.security.JwtFilter;

import java.util.Set;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${app.admin.email}")
    private String ADMIN_EMAIL;
    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtFilter jwtFilter,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'")))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/user/me").authenticated()
                                .requestMatchers(HttpMethod.GET, "/book", "/genre", "/volume", "/user", "/chapter").permitAll()
                                .requestMatchers(HttpMethod.GET, "/book/**", "/genre/*", "/volume/**", "/user/*", "/chapter/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.POST, "/genre").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/genre/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
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

    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean(JwtFilter jwtFilter){
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>(jwtFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}
