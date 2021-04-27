package chess.websocket.domain;

import chess.domain.room.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("socket")
public class Lobby {
    private final List<User> users = new ArrayList<>();

    public void enter(User user) {
        users.add(user);
    }

    public void leave(User user) {
        users.remove(user);
    }

    public void updateRoom(String rooms) {
        users.forEach(user -> user.sendData(rooms));
    }

    public List<User> users() {
        return users;
    }
}
