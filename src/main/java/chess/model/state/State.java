package chess.model.state;

import chess.dto.request.MoveRequest;
import chess.model.Team;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.Map;

public interface State {

    State proceed(MoveRequest moveRequest);

    Map<Position, Piece> getBoard();

    Map<String, Double> getScores();

    String getWinner();

    String getSymbol();
}
