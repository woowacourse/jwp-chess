package chess.repository;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface PieceRepository {
    ChessBoard findChessBoardByGameId(long gameId);

    void savePieces(ChessGameManager chessGameManager, long gameId);

    Piece findPieceByPosition(Position position, long gameId);

    void deletePieceByPosition(Position position, long gameId);

    void updatePiecePosition(Position fromPosition, Position toPosition, long gameId);
}
