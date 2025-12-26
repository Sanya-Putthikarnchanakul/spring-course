package com.example.demo.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_KEY = "correlationId";
    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    /**
     * Extracted 'X-Correlation-Id' header send from client and add it to logging
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String xCorrelationIdKey = "X-Correlation-Id";

        String xCorrelationId = request.getHeader(xCorrelationIdKey);
        String correlationId = Optional
                .ofNullable(xCorrelationId)
                .orElse(UUID.randomUUID().toString());

        log.info("{}={}", xCorrelationIdKey, correlationId);

        MDC.put(CORRELATION_ID_KEY, correlationId);
        response.setHeader(xCorrelationIdKey, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }

    }
}
