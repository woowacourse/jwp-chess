package chess.domain.state;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.PromotionPiece;
import chess.domain.piece.Piece;
import java.util.Map;

public interface ChessGameState {

    Turn nextTurn();

    Turn currentTurn();

    void movePiece(Position source, Position target);

    Position promotion(PromotionPiece promotionPiece);

    boolean isEnd();

    Map<Color, Double> currentScore();

    Map<Position, Piece> pieces();
}
