package com.tasksync.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        request.setAttribute(REQUEST_ID, requestId);
        request.setAttribute(START_TIME, System.currentTimeMillis());

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = getClientIp(request);

        log.info("[{}] ===== API Request =====", requestId);
        log.info("[{}] Method: {} {}", requestId, method, uri);
        if (queryString != null && !queryString.isEmpty()) {
            log.info("[{}] Query: {}", requestId, queryString);
        }
        log.info("[{}] Client: {}", requestId, remoteAddr);
        log.info("[{}] Content-Type: {}", requestId, request.getContentType());

        if (request instanceof CachedBodyHttpServletRequest cachedRequest) {
            String body = cachedRequest.getCachedBody();
            if (body != null && !body.isEmpty() && body.length() <= 2048) {
                try {
                    Object jsonBody = objectMapper.readValue(body, Object.class);
                    log.info("[{}] Body: {}", requestId, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonBody));
                } catch (Exception e) {
                    log.info("[{}] Body: {}", requestId, body);
                }
            }
        }

        log.info("[{}] =======================", requestId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestId = (String) request.getAttribute(REQUEST_ID);
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = System.currentTimeMillis() - startTime;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        log.info("[{}] ===== API Response =====", requestId);
        log.info("[{}] Method: {} {}", requestId, method, uri);
        log.info("[{}] Status: {} ({}ms)", requestId, status, duration);

        if (ex != null) {
            log.error("[{}] Exception: {}", requestId, ex.getMessage(), ex);
        }

        log.info("[{}] ========================", requestId);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
