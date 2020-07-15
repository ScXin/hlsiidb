package com.example.hlsiidb;

import com.example.hlsiidb.controller.RadiationWebSocketController;
import com.example.hlsiidb.controller.StatusWebSocketController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * 配置开放的WebSocket端口
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(radiationHandler(), "radiation").setAllowedOrigins("*");
        registry.addHandler(statusHandler(), "status").setAllowedOrigins("*");
    }

    public WebSocketHandler radiationHandler() {
        return new RadiationWebSocketController();
    }

    public WebSocketHandler statusHandler(){
        return new StatusWebSocketController();
    }
}
