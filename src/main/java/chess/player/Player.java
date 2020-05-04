package chess.player;

import chess.exception.InvalidConstructorValueException;
import chess.location.Location;
import chess.score.Score;
import chess.score.ScoreCalculator;
import chess.team.Team;

import java.util.Objects;

public class Player {
    private static final ScoreCalculator scoreCalculator = new ScoreCalculator();

    private final ChessSet chessSet;
    private final Team team;

    public Player(ChessSet chessSet, Team team) {
        validNullValue(chessSet, team);
        this.chessSet = chessSet;
        this.team = team;
    }

    private void validNullValue(ChessSet chessSet, Team team) {
        if (Objects.isNull(chessSet) || Objects.isNull(team)) {
            throw new InvalidConstructorValueException();
        }
    }

    public Score calculate() {
        return scoreCalculator.calculate(chessSet);
    }

    public boolean is(Team team) {
        return this.team.equals(team);
    }

    public boolean isNotSame(Team team) {
        return !is(team);
    }

    public void deletePieceIfExistIn(Location location) {
        chessSet.remove(location);
    }

    public boolean hasNotKing() {
        return chessSet.hasNotKing();
    }

    public void movePiece(Location now, Location after) {
        chessSet.movePiece(now, after);
    }

    public Team getTeam() {
        return team;
    }

    public String getTeamName() {
        return team.name();
    }
}


