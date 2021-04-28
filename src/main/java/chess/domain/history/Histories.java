package chess.domain.history;

import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;

import java.util.List;

public class Histories {

    private final List<History> histories;

    public Histories(List<History> histories) {
        this.histories = histories;
    }

    public void restoreChessBoardAsLatest(ChessBoard chessBoard) {
        histories.forEach(history -> history.updateChessBoard(chessBoard));
    }

    public TeamType findNextTeamType() {
        if (histories.isEmpty()) {
            return TeamType.WHITE;
        }
        int historyCounts = histories.size();
        String teamTypeValue = histories.get(historyCounts - 1)
                .getTeamType();
        return TeamType.valueOf(teamTypeValue)
                .findOppositeTeam();
    }
}
