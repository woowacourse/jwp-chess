package spring.chess.player;

import spring.chess.game.ChessSet;
import spring.chess.location.Location;
import spring.chess.score.Score;
import spring.chess.score.ScoreCalculator;
import spring.chess.team.Team;

public class Player {
    private static final ScoreCalculator scoreCalculator = new ScoreCalculator();

    private final ChessSet chessSet;
    private final Team team;

    public Player(ChessSet chessSet, Team team) {
        this.chessSet = chessSet;
        this.team = team;
    }

    public Score calculate() {
        return scoreCalculator.calculate(chessSet);
    }

    Score calculateScoreExceptPawnReduce() {
        return chessSet.calculateScoreExceptPawnReduce();
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


