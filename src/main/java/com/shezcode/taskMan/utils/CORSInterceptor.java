package com.shezcode.taskMan.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class CORSInterceptor implements Filter {

    private static final String[] allowedOrigins = {
            "http://localhost:3000", "http://localhost:5500", "http://localhost:5501",
            "http://127.0.0.1:3000", "http://127.0.0.1:5500", "http://127.0.0.1:5501"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Get the Origin header
        String requestOrigin = request.getHeader("Origin");

        // Check if the Origin header is present and valid
        if (requestOrigin != null && isAllowedOrigin(requestOrigin)) {
            // Authorize the origin, all headers, and all methods
            response.addHeader("Access-Control-Allow-Origin", requestOrigin);
            response.addHeader("Access-Control-Allow-Headers", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE");
            response.addHeader("Access-Control-Allow-Credentials", "true");

            // CORS handshake (pre-flight request)
            if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return;
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private boolean isAllowedOrigin(String origin) {
        for (String allowedOrigin : allowedOrigins) {
            if (allowedOrigin.equals(origin)) {
                return true;
            }
        }
        return false;
    }
}




