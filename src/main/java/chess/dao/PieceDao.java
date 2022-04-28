package chess.dao;

import chess.entity.PieceEntity;
import java.util.List;

public interface PieceDao {
    void initBoard(int gameId);

    List<PieceEntity> getBoardByGameId(int id);

    void remove(int id);

    int update(PieceEntity piece, int gameId);
}
