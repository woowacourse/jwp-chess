package chess.domain.piece;

import chess.domain.ChessBoard;
import chess.domain.Position;

public interface PieceRule {

    PieceRule move(Position source, Position target, ChessBoard chessboard);

    double score();

    boolean isPawn();

    boolean isKing();

    String name();
}
