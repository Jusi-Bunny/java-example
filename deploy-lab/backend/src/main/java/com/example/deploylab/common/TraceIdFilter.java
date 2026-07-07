package com.example.deploylab.common;

import com.example.deploylab.config.DeployLabProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HexFormat;

@Component
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String INSTANCE_ID_HEADER = "X-Instance-Id";
    public static final String APP_VERSION_HEADER = "X-App-Version";

    private static final SecureRandom RANDOM = new SecureRandom();
    private final DeployLabProperties properties;

    public TraceIdFilter(DeployLabProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = request.getHeader(REQUEST_ID_HEADER);
        if (!StringUtils.hasText(traceId)) {
            traceId = generateTraceId();
        }

        MDC.put(TraceContext.TRACE_ID, traceId);
        MDC.put(TraceContext.INSTANCE_ID, properties.getInstanceId());
        response.setHeader(REQUEST_ID_HEADER, traceId);
        response.setHeader(INSTANCE_ID_HEADER, properties.getInstanceId());
        response.setHeader(APP_VERSION_HEADER, properties.getAppVersion());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceContext.TRACE_ID);
            MDC.remove(TraceContext.INSTANCE_ID);
        }
    }

    private static String generateTraceId() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
