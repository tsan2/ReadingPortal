package ru.anastasya.readingportal.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.anastasya.readingportal.models.User;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    public Long getId(){
        return user.getId();
    }

}
