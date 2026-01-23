package com.example.demo.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

// @Component
// @Order(1)
public class DynamicLogLevelFilter implements Filter {

    private static final String LOG_LEVEL_HEADER = "X-Log-Level";
    private static final ThreadLocal<Level> originalLogLevel = new ThreadLocal<>();

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestedLogLevel = httpRequest.getHeader(LOG_LEVEL_HEADER);

        if (requestedLogLevel != null && !requestedLogLevel.isEmpty()) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

            // Store original level
            originalLogLevel.set(rootLogger.getLevel());

            // Set new level
            try {
                Level newLevel = Level.toLevel(requestedLogLevel.toUpperCase());
                rootLogger.setLevel(newLevel);
            } catch (Exception e) {
                // Invalid level, ignore
            }
        }

        try {
            chain.doFilter(
                    request,
                    response
            );
        } finally {
            // Restore original level
            if (originalLogLevel.get() != null) {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
                rootLogger.setLevel(originalLogLevel.get());
                originalLogLevel.remove();
            }
        }
    }
}