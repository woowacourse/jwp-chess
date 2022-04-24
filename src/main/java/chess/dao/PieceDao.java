package chess.dao;

import chess.service.dto.BoardDto;
import chess.service.dto.PieceEntity;

public interface PieceDao {
    void initBoard(int gameId);

    BoardDto getBoardByGameId(int id);

    void remove(int id);

    void update(PieceEntity piece, int gameId);
}
