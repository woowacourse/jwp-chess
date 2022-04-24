package chess.dao;

import chess.dto.MoveRequestDto;
import java.util.Map;

public interface ChessDao {
    Map<String, String> getBoardByGameId(String gameId);

    void move(MoveRequestDto moveRequestDto);

    String getTurnByGameId(String gameId);
}
