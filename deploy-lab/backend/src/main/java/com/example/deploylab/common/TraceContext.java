package com.example.deploylab.common;

import org.slf4j.MDC;

public final class TraceContext {

    public static final String TRACE_ID = "traceId";
    public static final String INSTANCE_ID = "instanceId";

    private TraceContext() {
    }

    public static String traceId() {
        String traceId = MDC.get(TRACE_ID);
        return traceId == null ? "" : traceId;
    }
}
