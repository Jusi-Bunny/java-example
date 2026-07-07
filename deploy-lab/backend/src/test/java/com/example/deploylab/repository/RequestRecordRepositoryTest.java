package com.example.deploylab.repository;

import com.example.deploylab.config.DeployLabProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestRecordRepositoryTest {

    @Test
    void shouldKeepRequestRecordCountWithinLimit() {
        DeployLabProperties properties = new DeployLabProperties();
        properties.getStorage().setMaxRequestRecords(2);
        RequestRecordRepository repository = new RequestRecordRepository(properties);

        repository.save("1", "GET", "/a", 200, 1L, "127.0.0.1", "backend");
        repository.save("2", "GET", "/b", 200, 1L, "127.0.0.1", "backend");
        repository.save("3", "GET", "/c", 200, 1L, "127.0.0.1", "backend");

        assertThat(repository.findAll(10)).extracting("path").containsExactly("/c", "/b");
    }
}
