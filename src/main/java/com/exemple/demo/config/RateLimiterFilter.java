package com.exemple.demo.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.servlet.*;
// import javax.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

@Component
public class RateLimiterFilter implements Filter {

    // private final Map<String, AtomicInteger> requestCountPerIpAdress = new
    // ConcurrentHashMap<>();

    // nombre maximum de requète par minute
    private static final int MAX_REQUESTS_PER_MINUTE = 200;

    private LoadingCache<String, Integer> requestCountPerIpAdress;

    // Initialisation de notre mémoire cache
    public RateLimiterFilter() {
        super();
        requestCountPerIpAdress = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(
                new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        String clientIpAdress = servletRequest.getRemoteAddr();

        // verification
        if (isMaximumRequestsPerminuteExcedeed(clientIpAdress)) {
            servletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            servletResponse.getWriter().write("Nombre de réquête par minute atteint");
            return;
        }
        filter.doFilter(request, response);
        // requestCountPerIpAdress.get(clientIpAdress).decrementAndGet();
    }

    public boolean isMaximumRequestsPerminuteExcedeed(String clientId) {
        System.out.println("Id :" + clientId + "\n\n");
        Integer request = 0;
        request = requestCountPerIpAdress.get(clientId);

        if (request != null) {
            if (request > MAX_REQUESTS_PER_MINUTE) {
                requestCountPerIpAdress.asMap().remove(clientId);
                requestCountPerIpAdress.put(clientId, request);
                return true;
            }

        } else {
            request = 0;
        }
        request++;
        requestCountPerIpAdress.put(clientId, request);
        return false;

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void destroy() {
    }

}
