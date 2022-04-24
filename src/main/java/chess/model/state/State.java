package chess.model.state;

import java.util.List;
import java.util.Map;

import chess.model.Team;
import chess.model.piece.Piece;
import chess.model.position.Position;

public interface State {

    boolean isFinished();

    boolean isStatus();

    State proceed(List<String> command);

    Map<Position, Piece> getBoard();

    Map<Team, Double> getScores();

    Team getWinner();

    boolean isReady();

    boolean isWhiteTurn();

    boolean isBlackTurn();
}
