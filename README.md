# Dynamic Log Level for Spring Boot REST API

This guide explains how to implement dynamic log level changes based on HTTP headers for debugging specific requests in production without affecting all traffic.

## Overview

When investigating production issues, you can send requests with a special header (e.g., `X-Log-Level: DEBUG`) to enable debug logging for that specific request only, while other requests continue using the default log level.

## Prerequisites

- Spring Boot application with REST API
- Logback logging framework
- Spring Actuator (already configured)

## Step 1: Add Dependencies

Ensure you have these in your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Step 2: Choose Implementation Approach

### Option A: Direct Log Level Change (Simple but affects root logger)

Create a filter that temporarily changes the root logger level:

```java
// filepath: src/main/java/com/example/filter/DynamicLogLevelFilter.java
package com.example.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class DynamicLogLevelFilter implements Filter {

    private static final String LOG_LEVEL_HEADER = "X-Log-Level";
    private static final ThreadLocal<Level> originalLogLevel = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
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
            chain.doFilter(request, response);
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
```

### Option B: MDC-Based Approach (Recommended)

This approach uses MDC (Mapped Diagnostic Context) for better isolation and thread safety:

```java
// filepath: src/main/java/com/example/filter/MDCLogLevelFilter.java
package com.example.filter;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class MDCLogLevelFilter implements Filter {

    private static final String LOG_LEVEL_HEADER = "X-Log-Level";
    public static final String MDC_LOG_LEVEL_KEY = "dynamicLogLevel";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestedLogLevel = httpRequest.getHeader(LOG_LEVEL_HEADER);
        
        if (requestedLogLevel != null && !requestedLogLevel.isEmpty()) {
            MDC.put(MDC_LOG_LEVEL_KEY, requestedLogLevel.toUpperCase());
        }
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_LOG_LEVEL_KEY);
        }
    }
}
```

## Step 3: Update Logback Configuration

### For Option A (Direct Level Change)

Your existing `logback-spring.xml` will work as-is.

### For Option B (MDC-Based - Recommended)

Update your `logback-spring.xml` to include MDC-based filtering:

```xml
<!-- filepath: src/main/resources/logback-spring.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Dynatrace Appender (keep your existing configuration) -->
    <appender name="DYNATRACE" class="your.dynatrace.appender.Class">
        <!-- Your Dynatrace configuration -->
    </appender>

    <!-- Turbo Filter for dynamic log level based on MDC -->
    <turboFilter class="ch.qos.logback.classic.turbo.MDCFilter">
        <MDCKey>dynamicLogLevel</MDCKey>
        <Value>DEBUG</Value>
        <OnMatch>ACCEPT</OnMatch>
    </turboFilter>

    <turboFilter class="ch.qos.logback.classic.turbo.MDCFilter">
        <MDCKey>dynamicLogLevel</MDCKey>
        <Value>TRACE</Value>
        <OnMatch>ACCEPT</OnMatch>
    </turboFilter>

    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="DYNATRACE" />
    </root>
    
</configuration>
```

## Step 4: Add Security (Recommended for Production)

Create a configuration to control access:

```java
// filepath: src/main/java/com/example/config/DynamicLoggingConfig.java
package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dynamic.logging")
public class DynamicLoggingConfig {
    
    private boolean enabled = false;
    private String secret;
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
```

Update the filter to use security:

```java
// filepath: src/main/java/com/example/filter/SecureMDCLogLevelFilter.java
package com.example.filter;

import com.example.config.DynamicLoggingConfig;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class SecureMDCLogLevelFilter implements Filter {

    private static final String LOG_LEVEL_HEADER = "X-Log-Level";
    private static final String AUTH_HEADER = "X-Log-Secret";
    public static final String MDC_LOG_LEVEL_KEY = "dynamicLogLevel";

    @Autowired
    private DynamicLoggingConfig config;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestedLogLevel = httpRequest.getHeader(LOG_LEVEL_HEADER);
        String authSecret = httpRequest.getHeader(AUTH_HEADER);
        
        // Check if feature is enabled and authorized
        if (config.isEnabled() && 
            requestedLogLevel != null && 
            !requestedLogLevel.isEmpty() &&
            isAuthorized(authSecret)) {
            
            MDC.put(MDC_LOG_LEVEL_KEY, requestedLogLevel.toUpperCase());
        }
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_LOG_LEVEL_KEY);
        }
    }
    
    private boolean isAuthorized(String authSecret) {
        return authSecret != null && 
               authSecret.equals(config.getSecret());
    }
}
```

## Step 5: Configure Application Properties

Add to your `application.properties` or `application.yml`:

```properties
# application.properties
dynamic.logging.enabled=true
dynamic.logging.secret=your-secret-key-here
```

Or in YAML:

```yaml
# application.yml
dynamic:
  logging:
    enabled: true
    secret: your-secret-key-here
```

## Step 6: Testing

### Test without security (Option A or B without security):

```bash
# Normal request (INFO level)
curl http://localhost:8080/api/your-endpoint

# Debug level request
curl -H "X-Log-Level: DEBUG" http://localhost:8080/api/your-endpoint

# Trace level request
curl -H "X-Log-Level: TRACE" http://localhost:8080/api/your-endpoint
```

### Test with security enabled:

```bash
# Without secret (will be ignored)
curl -H "X-Log-Level: DEBUG" http://localhost:8080/api/your-endpoint

# With correct secret
curl -H "X-Log-Level: DEBUG" \
     -H "X-Log-Secret: your-secret-key-here" \
     http://localhost:8080/api/your-endpoint
```

## Step 7: Verify in Your Code

Add debug logs in your controllers/services to verify:

```java
// filepath: src/main/java/com/example/controller/YourController.java
package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class YourController {

    private static final Logger logger = LoggerFactory.getLogger(YourController.class);

    @GetMapping("/your-endpoint")
    public String yourEndpoint() {
        logger.trace("TRACE level log");
        logger.debug("DEBUG level log");
        logger.info("INFO level log");
        logger.warn("WARN level log");
        
        return "Success";
    }
}
```

## Monitoring with Spring Actuator

You can also use Spring Actuator to change log levels globally (affects all requests):

```bash
# View current log levels
curl http://localhost:8080/actuator/loggers

# Change specific logger level
curl -X POST http://localhost:8080/actuator/loggers/com.example \
     -H "Content-Type: application/json" \
     -d '{"configuredLevel": "DEBUG"}'

# Reset to default
curl -X POST http://localhost:8080/actuator/loggers/com.example \
     -H "Content-Type: application/json" \
     -d '{"configuredLevel": null}'
```

## Best Practices

1. **Use MDC-Based Approach**: More thread-safe and doesn't affect concurrent requests
2. **Enable Security**: Always use authentication in production
3. **Rotate Secrets**: Change the secret key regularly
4. **Monitor Usage**: Log when dynamic logging is triggered
5. **Set Timeout**: Consider adding request timeout for debug requests
6. **Limit Scope**: Apply dynamic logging only to specific packages if possible

## Troubleshooting

### Logs not appearing at DEBUG level

- Check if logback-spring.xml is properly configured
- Verify the filter is registered (check with `@Order(1)`)
- Ensure MDC turbo filters are added in logback configuration

### Security not working

- Verify `dynamic.logging.enabled=true` in application.properties
- Check if the secret matches between configuration and request header
- Ensure `@Autowired` DynamicLoggingConfig is properly injected

### Performance concerns

- Use MDC-based approach to minimize impact
- Consider adding rate limiting for debug requests
- Monitor application performance when debug logging is active

## Integration with Dynatrace

Your existing Dynatrace logs configuration will continue to work. Debug logs from dynamic requests will also appear in Dynatrace with the appropriate log level.

Ensure your Dynatrace appender in logback-spring.xml includes all log levels:

```xml
<appender name="DYNATRACE" class="your.dynatrace.appender.Class">
    <!-- Include threshold to capture DEBUG logs -->
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
        <level>DEBUG</level>
    </filter>
</appender>
```

## Summary

This solution allows you to:
- ✅ Debug specific requests without affecting all traffic
- ✅ Control access with authentication
- ✅ Works with existing Dynatrace integration
- ✅ Thread-safe and production-ready
- ✅ Easy to enable/disable via configuration