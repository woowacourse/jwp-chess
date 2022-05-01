package chess.dao;

import chess.domain.entity.BoardPiece;
import java.util.List;
import java.util.Map;

public interface BoardPieceDao {
    void save(String gameId, Map<String, String> piecesByPositions);

    List<BoardPiece> findLastBoardPiece(String lastGameId);
}
