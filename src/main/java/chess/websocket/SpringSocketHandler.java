package chess.websocket;

import chess.domain.room.User;
import chess.websocket.commander.RequestCommander;
import chess.websocket.domain.SocketUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Profile("socket")
public class SpringSocketHandler extends TextWebSocketHandler {

    private final RequestCommander commander;
    private final ObjectMapper objectMapper;
    private final Map<String, User> users = new HashMap<>();

    public SpringSocketHandler(RequestCommander commander,
        ObjectMapper objectMapper) {
        this.commander = commander;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SocketUser user = new SocketUser(session);
        users.put(session.getId(), user);
        commander.enterLobby(user);
        commander.updateRoom(new HashMap<>(), user);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage input)
        throws Exception {
        RequestForm requestForm =
            objectMapper.readValue(input.getPayload(), RequestForm.class);

        User user = users.get(session.getId());

        requestForm.getCommand().execute(commander, requestForm.getData(), user);
        super.handleTextMessage(session, input);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {
        User user = users.get(session.getId());
        commander.leaveLobby(user);
        commander.leaveRoom(user);
        users.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}
