package chess.domain.history;

import chess.domain.board.ChessBoard;
import chess.domain.board.Coordinate;
import chess.domain.piece.TeamType;

import java.util.Objects;

public class History {

    private final String source;
    private final String destination;
    private final String teamType;

    public History(String source, String destination, String teamType) {
        this.source = source;
        this.destination = destination;
        this.teamType = teamType;
    }

    public void updateChessBoard(ChessBoard chessBoard) {
        chessBoard.move(Coordinate.from(source), Coordinate.from(destination), TeamType.valueOf(teamType));
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getTeamType() {
        return teamType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(source, history.source) && Objects.equals(destination, history.destination) && Objects.equals(teamType, history.teamType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, teamType);
    }
}
