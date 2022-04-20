package chess.dao;

import chess.dto.GameDto;

public interface GameDao {

    void save(GameDto gameDto);

    int findGameIdByUserName(String whiteUserName, String blackUserName);

    GameDto findById(int gameId);

    void update(String state, int gameId);

    void deleteById(int gameId);
}
