package ru.anastasya.readingportal.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.anastasya.readingportal.exceptions.TokenValidationException;
import ru.anastasya.readingportal.models.Role;
import ru.anastasya.readingportal.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(1);
        String accessToken = getToken(request);
        if (accessToken == null || !jwtProvider.validateToken(accessToken)){
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = jwtProvider.getClaims(accessToken);

        User user = new User();
        user.setNickname(claims.getSubject());
        user.setId(Long.valueOf((Integer) claims.get("userId")));
        ArrayList<Object> roleList = (ArrayList<Object>) claims.get("roles");
        Set<Role> roles = roleList.stream().map(s -> Role.valueOf(String.valueOf(s))).collect(Collectors.toSet());
        user.setRoles(roles);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.substring(7);
        }
        System.out.println(authorizationHeader);
        return authorizationHeader;
    }



}
