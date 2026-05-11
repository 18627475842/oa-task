package com.tasksync.filter;

import com.tasksync.interceptor.CachedBodyHttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestCachingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String contentType = httpRequest.getContentType();
            if (contentType != null && contentType.contains("application/json")) {
                CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);
                chain.doFilter(cachedRequest, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
