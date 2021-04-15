package chess.websocket.util;

import chess.websocket.commander.RequestCommander;
import java.io.IOException;
import org.springframework.web.socket.WebSocketSession;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(RequestCommander t, String[] u, WebSocketSession v) throws IOException;
}
