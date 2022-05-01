package chess.database.dao;

import chess.database.dto.GameStateDto;

public interface GameDao {

    GameStateDto readStateAndColor(int roomId);

    void updateState(GameStateDto gameStateDto, int roomId);

    void removeGame(int roomId);

    void create(GameStateDto gameStateDto, int roomId);
}
