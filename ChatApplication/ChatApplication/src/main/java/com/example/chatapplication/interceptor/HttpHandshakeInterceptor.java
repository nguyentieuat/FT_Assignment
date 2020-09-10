package com.example.chatapplication.interceptor;

import com.example.chatapplication.ultities.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        log.info("Call beforeHandshake");

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            String requestToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(Constant.COOKIE_NAME)).findFirst().map(Cookie::getValue)
                    .orElse(null);
            map.put(Constant.COOKIE_NAME, requestToken);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        log.info("Call afterHandshake");
    }
}
