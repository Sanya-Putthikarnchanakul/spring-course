package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class MDCLogLevelFilter implements Filter {

    public static final String MDC_LOG_LEVEL_KEY = "dynamicLogLevel";
    private static final String LOG_LEVEL_HEADER = "X-Log-Level";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestedLogLevel = httpRequest.getHeader(LOG_LEVEL_HEADER);

        if (requestedLogLevel != null && !requestedLogLevel.isEmpty()) {
            MDC.put(
                    MDC_LOG_LEVEL_KEY,
                    requestedLogLevel.toUpperCase()
            );
        }

        try {
            chain.doFilter(
                    request,
                    response
            );
        } finally {
            MDC.remove(MDC_LOG_LEVEL_KEY);
        }
    }

}