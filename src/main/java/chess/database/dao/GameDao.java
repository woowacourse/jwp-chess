package chess.database.dao;

import java.util.List;

import chess.database.dto.GameStateDto;

public interface GameDao {

//    List<String> readStateAndColor(String roomName);

    List<String> readStateAndColor(int roomId);

//    void saveGame(GameStateDto gameStateDto, String roomName);

//    void updateState(GameStateDto gameStateDto, String roomName);

    void updateState(GameStateDto gameStateDto, int roomId);

//    void removeGame(String roomName);

    void removeGame(int roomId);

    void create(GameStateDto gameStateDto, int roomId);
}
