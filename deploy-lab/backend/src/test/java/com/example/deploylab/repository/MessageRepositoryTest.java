package com.example.deploylab.repository;

import com.example.deploylab.config.DeployLabProperties;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class MessageRepositoryTest {

    @Test
    void shouldKeepMessageCountWithinLimit() {
        DeployLabProperties properties = new DeployLabProperties();
        properties.getStorage().setMaxMessages(2);
        MessageRepository repository = new MessageRepository(properties);

        repository.save("first", "", null);
        repository.save("second", "", null);
        repository.save("third", "", null);

        assertThat(repository.findAll(10)).extracting("title").containsExactly("third", "second");
    }

    @Test
    void shouldDeleteExpiredMessages() {
        DeployLabProperties properties = new DeployLabProperties();
        MessageRepository repository = new MessageRepository(properties);
        repository.save("expired", "", Instant.now().minusSeconds(1));
        repository.save("active", "", Instant.now().plusSeconds(60));

        int deleted = repository.deleteExpired(Instant.now());

        assertThat(deleted).isEqualTo(1);
        assertThat(repository.findAll(10)).extracting("title").containsExactly("active");
    }
}
