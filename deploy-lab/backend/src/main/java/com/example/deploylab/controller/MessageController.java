package com.example.deploylab.controller;

import com.example.deploylab.common.ApiResponse;
import com.example.deploylab.dto.request.CreateMessageRequest;
import com.example.deploylab.model.Message;
import com.example.deploylab.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ApiResponse<List<Message>> list(@RequestParam(defaultValue = "50") @Min(1) @Max(200) int limit) {
        return ApiResponse.success(messageService.list(limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Message> create(@Valid @RequestBody CreateMessageRequest request) {
        return ApiResponse.success(messageService.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<Message> get(@PathVariable Long id) {
        return ApiResponse.success(messageService.get(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        messageService.delete(id);
        return ApiResponse.success(null);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteAll() {
        messageService.deleteAll();
        return ApiResponse.success(null);
    }
}
