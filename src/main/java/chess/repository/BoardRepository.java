package chess.repository;

import chess.domain.Color;
import chess.web.dto.BoardDto;
import chess.web.dto.GameStateDto;
import java.util.Optional;

public interface BoardRepository {
    int save(int roomId, GameStateDto gameStateDto);

    Color getTurn(int boardId);

    Optional<Integer> findBoardIdByRoom(int roomId);

    void updateTurn(int boardId, GameStateDto gameStateDto);

    void deleteByRoom(int roomId);
}
