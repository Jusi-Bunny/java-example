package com.example.satoken.common.exception;

/**
 * @author Jusi
 * @date 2026/8/11
 */
public class AccountDisabledException extends RuntimeException {

    public AccountDisabledException(String message) {
        super(message);
    }
}
