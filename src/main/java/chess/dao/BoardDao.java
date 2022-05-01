package chess.dao;

import chess.service.dto.BoardDto;
import chess.service.dto.PieceWithSquareDto;

public interface BoardDao {
    void initBoard(Long gameId);

    BoardDto getBoardByGameId(Long id);

    void remove(Long id);

    void update(PieceWithSquareDto piece, Long gameId);
}
