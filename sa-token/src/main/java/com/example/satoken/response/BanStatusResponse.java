package com.example.satoken.response;

import lombok.Data;

/**
 * @author Jusi
 * @date 2026/10/10
 */
@Data
public class BanStatusResponse {

    private Boolean disabled;

    private Long remainingSeconds;
}
