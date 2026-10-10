package com.example.satoken.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jusi
 * @date 2026/10/10
 */
@Data
public class SafeStatusResponse {

    private Boolean safe;

    private Long remainingSeconds;
}
