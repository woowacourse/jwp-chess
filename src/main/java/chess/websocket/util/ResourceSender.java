package chess.websocket.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ResourceSender {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void send(WebSocketSession session, T resource) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(resource)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
