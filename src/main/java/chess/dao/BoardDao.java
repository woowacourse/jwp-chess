package chess.dao;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface BoardDao {

    void save(ChessBoard chessBoard, int gameId);

    ChessBoard findById(int id);

    int update(Position position, Piece piece, int gameId);

    void delete(int gameId);
}
