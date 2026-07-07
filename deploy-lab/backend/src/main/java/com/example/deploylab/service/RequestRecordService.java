package com.example.deploylab.service;

import com.example.deploylab.model.RequestRecord;
import com.example.deploylab.repository.RequestRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestRecordService {

    private final RequestRecordRepository repository;

    public RequestRecordService(RequestRecordRepository repository) {
        this.repository = repository;
    }

    public RequestRecord create(String traceId, String method, String path, Integer status,
                                Long durationMs, String clientIp, String instanceId) {
        return repository.save(traceId, method, path, status, durationMs, clientIp, instanceId);
    }

    public List<RequestRecord> list(int limit) {
        return repository.findAll(limit);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
