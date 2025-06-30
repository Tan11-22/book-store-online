package com.BookStore.AuthenticationService.exceptions;

public enum AuthErrorCode {
    INVALID_CREDENTIALS("AUTH_001", "Invalid username or password"),
    ACCOUNT_LOCKED("AUTH_002", "Account is locked"),
    TOKEN_EXPIRED("AUTH_003", "Token has expired"),
    UNAUTHORIZED("AUTH_004", "Unauthorized access"),
    USER_NOT_FOUND("AUTH_005", "User not found"),
    OAUTH2_LOGIN_FAILED("AUTH_006", "OAuth2 login failed"),
    EMAIL_NOT_VERIFIED("AUTH_007", "Email not verified");
    AuthErrorCode(String auth001, String s) {
    }
}
