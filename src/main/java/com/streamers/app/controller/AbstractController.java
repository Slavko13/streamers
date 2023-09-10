package com.streamers.app.controller;

import com.streamers.app.dto.auth.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.DatatypeConverter;



public abstract class AbstractController {

    @Autowired
    private HttpServletRequest request;

    protected UserDto getCurrentUser() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Удаляем префикс "Bearer "

            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary("YOUR_SECRET_KEY")) // Используйте ваш секретный ключ, который использовался для подписи JWT.
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                UserDto userDto = new UserDto();
                userDto.setUsername(username);

                return userDto;
            } catch (SignatureException | MalformedJwtException e) {
                throw new SecurityException("Invalid JWT token", e);
            }
        } else {
            throw new SecurityException("JWT token is missing or invalid");
        }
    }
}

