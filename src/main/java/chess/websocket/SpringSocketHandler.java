package chess.websocket;

import chess.websocket.commander.RequestCommand;
import chess.websocket.commander.RequestCommander;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Profile("socket")
public class SpringSocketHandler extends TextWebSocketHandler {

    private final RequestCommander commander = new RequestCommander();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage input)
        throws Exception {

        String message = input.getPayload();
        if (message.startsWith("<request>")) {
            RequestCommand.execute(commander, message.split(" "), session);
        } else {
            commander.sendMessage(session, message);
        }
        super.handleTextMessage(session, input);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {
        commander.leaveRoom(session);
        super.afterConnectionClosed(session, status);
    }
}
