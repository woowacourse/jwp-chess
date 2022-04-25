package chess.domain.state;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.PromotionPiece;
import chess.domain.piece.Piece;
import java.util.Map;

public interface ChessGameState {

    Turn nextTurn();

    void movePiece(Position source, Position target);

    Position promotion(PromotionPiece promotionPiece);

    Map<Color, Double> currentScore();

    Map<Position, Piece> pieces();
}
