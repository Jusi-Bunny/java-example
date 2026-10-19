package com.example.satoken.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Jusi
 * @date 2026/10/19
 */
@Data
public class BanLevelRequest {

    @NotNull(message = "等级不能为空")
    @Min(value = 1, message = "等级只能为1或2")
    @Max(value = 2, message = "等级只能为1或2")
    private Integer level;
}
