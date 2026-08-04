package com.example.i18n.web;

import com.example.i18n.config.I18nMessageService;
import com.example.i18n.error.BusinessException;
import com.example.i18n.error.ErrorCode;
import jakarta.validation.Valid;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo/users")
@RequiredArgsConstructor
public class DemoUserController {

    private final I18nMessageService i18n;

    @GetMapping("/greeting")
    public Map<String, String> greeting(@RequestParam(defaultValue = "Jusi") String name) {

        return Map.of("message", i18n.get("greeting", name));
    }

    @GetMapping("/{id}")
    public Map<String, String> findById(@PathVariable String id) {
        if ("missing".equals(id)) {
            // throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, id);
        }
        return Map.of("id", id, "name", "Ada");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> create(@Valid @RequestBody CreateUserRequest request) {
        return Map.of("name", request.getName());
    }
}
