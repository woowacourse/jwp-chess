package chess.repository;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface PieceRepository {
    ChessBoard findChessBoardByGameId(int gameId);

    void savePieces(ChessGameManager chessGameManager, int gameId);

    void savePiece(Piece piece, Position position, int gameId);

    Piece findPieceByPosition(Position position, int gameId);

    void deletePieceByPosition(Position position, int gameId);
}
