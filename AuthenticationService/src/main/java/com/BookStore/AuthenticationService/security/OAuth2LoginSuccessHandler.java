package com.BookStore.AuthenticationService.security;

import com.BookStore.AuthenticationService.jwt.JwtTokenProvider;
import com.BookStore.AuthenticationService.model.AccountEntity;
import com.BookStore.AuthenticationService.security.CustomOAuth2User;
import com.BookStore.AuthenticationService.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getEmail();
        System.out.println(email);
        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);
        
        // Save refresh token to database
        accountRepository.findByEmail(email).ifPresent(account -> {
            account.setRefreshToken(refreshToken);
            accountRepository.save(account);
        });
        
        // Set secure HTTP-only cookies
        Cookie accessTokenCookie = createSecureCookie("access_token", accessToken, 15 * 60); // 15 phút
        Cookie refreshTokenCookie = createSecureCookie("refresh_token", refreshToken, 7 * 24 * 60 * 60); // 7 ngày
        refreshTokenCookie.setHttpOnly(true); // Chỉ server có thể đọc
        
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        
        // Redirect (không có token trong URL)
        response.sendRedirect("http://localhost:3000/oauth2/redirect");
    }

    private Cookie createSecureCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }
}
