package chess.model.state;

import chess.dto.MoveDto;
import chess.model.Team;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.Map;

public interface State {

    State proceed(MoveDto moveDto);

    Map<Position, Piece> getBoard();

    Map<Team, Double> getScores();

    Team getWinner();

    String getSymbol();
}
