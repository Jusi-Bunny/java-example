package com.example.deploylab.service;

import com.example.deploylab.config.DeployLabProperties;
import com.example.deploylab.dto.response.HttpInspectResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class HttpInspectService {

    private static final int MAX_BODY_DISPLAY_LENGTH = 10 * 1024;
    private static final List<String> SENSITIVE_HEADERS = List.of(
            "authorization",
            "cookie",
            "set-cookie",
            "proxy-authorization"
    );

    private final DeployLabProperties properties;
    private final ClientIpResolver clientIpResolver;

    public HttpInspectService(DeployLabProperties properties, ClientIpResolver clientIpResolver) {
        this.properties = properties;
        this.clientIpResolver = clientIpResolver;
    }

    public HttpInspectResponse inspect(HttpServletRequest request, String body) {
        String safeBody = body == null ? "" : body;
        boolean truncated = safeBody.length() > MAX_BODY_DISPLAY_LENGTH;
        if (truncated) {
            safeBody = safeBody.substring(0, MAX_BODY_DISPLAY_LENGTH);
        }

        return new HttpInspectResponse(
                request.getMethod(),
                request.getRequestURI(),
                queryParameters(request),
                headers(request),
                request.getContentType(),
                safeBody,
                truncated,
                clientIpResolver.resolve(request),
                request.getHeader("X-Real-IP"),
                request.getHeader("X-Forwarded-For"),
                request.getHeader("X-Forwarded-Proto"),
                request.getHeader("Host"),
                request.getHeader("User-Agent"),
                properties.getInstanceId(),
                Instant.now()
        );
    }

    private Map<String, List<String>> queryParameters(HttpServletRequest request) {
        Map<String, List<String>> parameters = new LinkedHashMap<>();
        request.getParameterMap().forEach((key, values) -> parameters.put(key, List.of(values)));
        return parameters;
    }

    private Map<String, List<String>> headers(HttpServletRequest request) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return result;
        }
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            List<String> values = Collections.list(request.getHeaders(name));
            if (SENSITIVE_HEADERS.contains(name.toLowerCase(Locale.ROOT))) {
                values = List.of("******");
            }
            result.put(name, values);
        }
        return result;
    }
}
