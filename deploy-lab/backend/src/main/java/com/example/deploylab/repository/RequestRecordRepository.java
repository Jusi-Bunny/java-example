package com.example.deploylab.repository;

import com.example.deploylab.config.DeployLabProperties;
import com.example.deploylab.model.RequestRecord;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RequestRecordRepository {

    private final ConcurrentHashMap<Long, RequestRecord> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    private final DeployLabProperties properties;

    public RequestRecordRepository(DeployLabProperties properties) {
        this.properties = properties;
    }

    public RequestRecord save(String traceId, String method, String path, Integer status,
                              Long durationMs, String clientIp, String instanceId) {
        RequestRecord record = new RequestRecord(
                sequence.incrementAndGet(),
                traceId,
                method,
                path,
                status,
                durationMs,
                clientIp,
                instanceId,
                Instant.now()
        );
        storage.put(record.id(), record);
        trimToLimit();
        return record;
    }

    public List<RequestRecord> findAll(int limit) {
        int resolvedLimit = Math.max(1, Math.min(limit, properties.getStorage().getMaxRequestRecords()));
        return storage.values().stream()
                .sorted(Comparator.comparing(RequestRecord::createdAt).thenComparing(RequestRecord::id).reversed())
                .limit(resolvedLimit)
                .toList();
    }

    public void deleteAll() {
        storage.clear();
    }

    private void trimToLimit() {
        int maxRecords = Math.max(1, properties.getStorage().getMaxRequestRecords());
        int overflow = storage.size() - maxRecords;
        if (overflow <= 0) {
            return;
        }
        storage.values().stream()
                .sorted(Comparator.comparing(RequestRecord::createdAt).thenComparing(RequestRecord::id))
                .limit(overflow)
                .map(RequestRecord::id)
                .toList()
                .forEach(storage::remove);
    }
}
