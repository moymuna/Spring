package com.emranhss.HRM_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final long WINDOW_MS = 60_000; 

    private record Limit(String path, int maxRequests) {
    }

    private static final Limit[] LIMITS = {
            new Limit("/api/auth/login", 10),
            new Limit("/api/auth/register-applicant", 5),
            new Limit("/api/auth/forgot-password", 3),
    };

    private static class Counter {
        final AtomicInteger count = new AtomicInteger(0);
        volatile long windowStart = System.currentTimeMillis();
    }

    private final Map<String, Counter> counters = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Limit limit = matchLimit(request.getRequestURI());
        if (limit == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = clientIp(request) + ":" + limit.path();
        Counter counter = counters.computeIfAbsent(key, k -> new Counter());

        long now = System.currentTimeMillis();
        synchronized (counter) {
            if (now - counter.windowStart > WINDOW_MS) {
                counter.windowStart = now;
                counter.count.set(0);
            }
        }

        if (counter.count.incrementAndGet() > limit.maxRequests()) {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Too many attempts. Please wait a minute and try again.\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Limit matchLimit(String requestUri) {
        for (Limit limit : LIMITS) {
            if (requestUri.endsWith(limit.path())) {
                return limit;
            }
        }
        return null;
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    
    
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cleanupStaleCounters() {
        long cutoff = System.currentTimeMillis() - (10 * 60 * 1000);
        counters.values().removeIf(counter -> counter.windowStart < cutoff);
    }
}
