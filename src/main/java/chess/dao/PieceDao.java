package chess.dao;

import chess.entity.PieceEntity;
import java.util.List;

public interface PieceDao {
    void initBoard(Integer gameId);

    List<PieceEntity> getBoardByGameId(Integer gameId);

    void remove(Integer id);

    int update(PieceEntity piece, Integer gameId);
}
