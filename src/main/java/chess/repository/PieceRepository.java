package chess.repository;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface PieceRepository {
    void savePiecesByGameId(ChessGameManager chessGameManager, int gameId);

    ChessBoard loadChessBoardByGameId(int gameId);

    Piece loadPieceByPosition(Position position, int gameId);

    void savePiece(Piece piece, Position position, int gameId);

    void deletePieceByPosition(Position position, int gameId);
}
