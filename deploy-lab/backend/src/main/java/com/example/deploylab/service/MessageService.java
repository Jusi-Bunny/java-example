package com.example.deploylab.service;

import com.example.deploylab.common.BusinessException;
import com.example.deploylab.common.ErrorCode;
import com.example.deploylab.dto.request.CreateMessageRequest;
import com.example.deploylab.model.Message;
import com.example.deploylab.repository.MessageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> list(int limit) {
        return messageRepository.findAll(limit);
    }

    public Message create(CreateMessageRequest request) {
        return messageRepository.save(request.title().trim(), request.content(), request.expiresAt());
    }

    public Message get(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "消息不存在"));
    }

    public void delete(Long id) {
        if (!messageRepository.deleteById(id)) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "消息不存在");
        }
    }

    public void deleteAll() {
        messageRepository.deleteAll();
    }

    @Scheduled(fixedDelay = 60_000)
    public void deleteExpired() {
        messageRepository.deleteExpired(Instant.now());
    }
}
