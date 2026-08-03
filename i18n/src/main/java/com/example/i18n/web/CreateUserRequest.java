package com.example.i18n.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "{validation.user.name.required}")
    @Size(min = 2, max = 20, message = "{validation.user.name.size}")
    private String name;

}
