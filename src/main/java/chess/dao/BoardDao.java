package chess.dao;

import chess.domain.board.Board;
import chess.domain.game.GameId;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface BoardDao {
    Board getBoard(GameId gameId);

    void createPiece(GameId gameId, Position position, Piece piece);

    void deletePiece(GameId gameId, Position position);

    void updatePiecePosition(GameId gameId, Position from, Position to);
}
