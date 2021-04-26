package chess.websocket.util;

import chess.domain.room.User;
import chess.websocket.commander.RequestCommander;
import java.io.IOException;
import java.util.Map;
import org.springframework.web.socket.WebSocketSession;

public enum Command {
    CREATE_ROOM(RequestCommander::createRoom),
    LOAD_ROOM(RequestCommander::updateRoom);

    private final TriConsumer<RequestCommander, Map<String, Object>, User> consumer;

    Command(TriConsumer<RequestCommander, Map<String, Object>, User> consumer) {
        this.consumer = consumer;
    }

    public void execute(RequestCommander commander, Map<String, Object> data, User user)
        throws Exception {
        try {
            consumer.accept(commander, data, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
