package com.example.deploylab.interceptor;

import com.example.deploylab.common.TraceContext;
import com.example.deploylab.config.DeployLabProperties;
import com.example.deploylab.service.ClientIpResolver;
import com.example.deploylab.service.RequestRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestRecordInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestRecordInterceptor.class);
    private static final String START_TIME_ATTRIBUTE = RequestRecordInterceptor.class.getName() + ".startTime";

    private final RequestRecordService requestRecordService;
    private final ClientIpResolver clientIpResolver;
    private final DeployLabProperties properties;

    public RequestRecordInterceptor(RequestRecordService requestRecordService,
                                    ClientIpResolver clientIpResolver,
                                    DeployLabProperties properties) {
        this.requestRecordService = requestRecordService;
        this.clientIpResolver = clientIpResolver;
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (shouldSkip(request)) {
            return;
        }
        long startTime = (long) request.getAttribute(START_TIME_ATTRIBUTE);
        long durationMs = System.currentTimeMillis() - startTime;
        String path = request.getRequestURI();

        requestRecordService.create(
                TraceContext.traceId(),
                request.getMethod(),
                path,
                response.getStatus(),
                durationMs,
                clientIpResolver.resolve(request),
                properties.getInstanceId()
        );

        log.info("Request completed method={} path={} status={} duration={}ms",
                request.getMethod(), path, response.getStatus(), durationMs);
    }

    private boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "/actuator/health".equals(path) || path.startsWith("/error");
    }
}
