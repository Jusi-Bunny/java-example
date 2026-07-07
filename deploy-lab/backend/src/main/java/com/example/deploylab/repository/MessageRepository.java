package com.example.deploylab.repository;

import com.example.deploylab.config.DeployLabProperties;
import com.example.deploylab.model.Message;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MessageRepository {

    private final ConcurrentHashMap<Long, Message> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    private final DeployLabProperties properties;

    public MessageRepository(DeployLabProperties properties) {
        this.properties = properties;
    }

    public Message save(String title, String content, Instant expiresAt) {
        Message message = new Message(sequence.incrementAndGet(), title, content, Instant.now(), expiresAt);
        storage.put(message.id(), message);
        trimToLimit();
        return message;
    }

    public List<Message> findAll(int limit) {
        int resolvedLimit = Math.max(1, Math.min(limit, properties.getStorage().getMaxMessages()));
        return storage.values().stream()
                .sorted(Comparator.comparing(Message::createdAt).thenComparing(Message::id).reversed())
                .limit(resolvedLimit)
                .toList();
    }

    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public void deleteAll() {
        storage.clear();
    }

    public int deleteExpired(Instant now) {
        List<Long> expiredIds = storage.values().stream()
                .filter(message -> message.expiresAt() != null && !message.expiresAt().isAfter(now))
                .map(Message::id)
                .toList();
        expiredIds.forEach(storage::remove);
        return expiredIds.size();
    }

    private void trimToLimit() {
        int maxMessages = Math.max(1, properties.getStorage().getMaxMessages());
        int overflow = storage.size() - maxMessages;
        if (overflow <= 0) {
            return;
        }
        storage.values().stream()
                .sorted(Comparator.comparing(Message::createdAt).thenComparing(Message::id))
                .limit(overflow)
                .map(Message::id)
                .toList()
                .forEach(storage::remove);
    }
}
