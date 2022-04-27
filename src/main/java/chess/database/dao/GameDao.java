package chess.database.dao;

import chess.database.dto.GameStateDto;
import java.util.List;

public interface GameDao {

    List<String> readStateAndColor(int roomId);

    void updateState(GameStateDto gameStateDto, int roomId);

    void removeGame(int roomId);

    void create(GameStateDto gameStateDto, int roomId);
}
