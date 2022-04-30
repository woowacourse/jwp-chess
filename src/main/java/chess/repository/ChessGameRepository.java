package chess.repository;

import chess.domain.ChessGame;
import chess.domain.position.Position;

public interface ChessGameRepository {
    ChessGame get(int roomId);

    void update(int roomId, Position from, Position to);

    void add(int roomId, ChessGame chessGame);
}
