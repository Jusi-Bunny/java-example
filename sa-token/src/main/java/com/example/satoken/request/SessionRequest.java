package com.example.satoken.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Jusi
 * @date 2026/10/9
 */
@Data
public class SessionRequest {

    @NotBlank
    @Size(max = 50)
    String sharedLabel;

    @NotBlank
    @Size(max = 50)
    String localStep;
}
