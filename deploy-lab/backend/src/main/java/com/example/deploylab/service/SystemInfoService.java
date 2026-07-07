package com.example.deploylab.service;

import com.example.deploylab.config.DeployLabProperties;
import com.example.deploylab.dto.response.SystemInfoResponse;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@Service
public class SystemInfoService {

    private final Instant startedAt = Instant.now();
    private final DeployLabProperties properties;
    private final Environment environment;
    private final ObjectProvider<WebServerApplicationContext> webServerApplicationContext;
    private final Optional<BuildProperties> buildProperties;

    public SystemInfoService(DeployLabProperties properties, Environment environment,
                             ObjectProvider<WebServerApplicationContext> webServerApplicationContext,
                             Optional<BuildProperties> buildProperties) {
        this.properties = properties;
        this.environment = environment;
        this.webServerApplicationContext = webServerApplicationContext;
        this.buildProperties = buildProperties;
    }

    public SystemInfoResponse getInfo() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        Instant now = Instant.now();
        return new SystemInfoResponse(
                environment.getProperty("spring.application.name", "deploy-lab"),
                buildProperties.map(BuildProperties::getVersion).orElse(properties.getAppVersion()),
                properties.getInstanceId(),
                activeProfile(),
                startedAt,
                Duration.between(startedAt, now).toSeconds(),
                System.getProperty("java.version"),
                System.getProperty("os.name"),
                hostname(),
                environmentValue("HOSTNAME", "COMPUTERNAME"),
                actualPort(),
                now,
                runtime.availableProcessors(),
                usedMemory,
                runtime.maxMemory(),
                properties.getDiagnostics().isEnabled()
        );
    }

    private String activeProfile() {
        String[] profiles = environment.getActiveProfiles();
        return profiles.length == 0 ? "default" : String.join(",", profiles);
    }

    private String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    private String environmentValue(String... names) {
        return Arrays.stream(names)
                .map(System::getenv)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse("");
    }

    private int actualPort() {
        WebServerApplicationContext context = webServerApplicationContext.getIfAvailable();
        if (context != null && context.getWebServer() != null) {
            return context.getWebServer().getPort();
        }
        return environment.getProperty("server.port", Integer.class, 8080);
    }
}
