package chess.dao;

import chess.domain.db.BoardPiece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeBoardPieceDao implements BoardPieceDao{
    @Override
    public void save(String gameId, Map<String, String> piecesByPositions) {

    }

    @Override
    public List<BoardPiece> findLastBoardPiece(String lastGameId) {
        return new ArrayList<>(List.of(new BoardPiece("", "", "", ".")));
    }
}
