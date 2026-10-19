package com.example.satoken.response;

import lombok.Data;

/**
 * @author Jusi
 * @date 2026/10/19
 */
@Data
public class BanLevelStatusResponse {

    private Integer level;

    private Long remainingSeconds;
}
