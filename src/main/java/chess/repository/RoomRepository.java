package chess.repository;

import chess.domain.Room;
import chess.dto.GameCreateRequest;
import java.util.List;

public interface RoomRepository {

    long save(GameCreateRequest gameCreateRequest);

    Room findById(long id);

    List<Room> findAll();

    long deleteById(long id);

    long deleteAll();

    void changeTurnById(long id);

    void finishById(long id);
}
