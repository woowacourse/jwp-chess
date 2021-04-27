package chess.config;

import chess.websocket.SpringSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Profile("socket")
public class SocketWebConfiguration implements WebMvcConfigurer, WebSocketConfigurer {


    private final SpringSocketHandler springSocketHandler;

    public SocketWebConfiguration(SpringSocketHandler springSocketHandler) {
        this.springSocketHandler = springSocketHandler;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/temp/chess.html");
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(springSocketHandler, "/chess");
    }
}
