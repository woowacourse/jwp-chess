package chess.websocket.commander;

import chess.domain.room.User;
import chess.websocket.util.TriConsumer;
import java.io.IOException;
import java.util.Map;

public enum Command {
    CREATE_ROOM(RequestCommander::createRoom),
    LOAD_ROOM(RequestCommander::updateRoom),
    ENTER_ROOM_PLAYER(RequestCommander::enterRoomAsPlayer),
    ENTER_ROOM_PARTICIPANT(RequestCommander::enterRoomAsParticipant),
    PIECE_MOVE(RequestCommander::move),
    CHAT(RequestCommander::chat);

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
