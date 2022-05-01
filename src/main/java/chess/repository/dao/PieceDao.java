package chess.repository.dao;

import chess.repository.dao.entity.PieceEntity;
import java.util.List;

public interface PieceDao {
    void initBoard(Integer gameId);

    List<PieceEntity> getBoardByGameId(Integer gameId);

    int update(PieceEntity piece);
}
