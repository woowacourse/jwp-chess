package wooteco.chess.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.gameinfo.GameInfo;
import wooteco.chess.entity.Room;

@Component
public class GameRepository {

    private Map<Room, GameInfo> games = new HashMap<>();

    public void save(Room room, GameInfo gameInfo) {
        games.put(room, gameInfo);
    }

    public void delete(Room room) {
        games.remove(room);
    }

    public GameInfo findByRoom(Room room) {
        return games.get(room);
    }
}
