package chess.repository;

import chess.domain.chess.Chess;

public interface ChessRepository {
    Chess findChessById(long chessId);
}
