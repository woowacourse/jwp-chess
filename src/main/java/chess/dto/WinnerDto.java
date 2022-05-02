package chess.dto;

import chess.domain.board.Result;
import chess.domain.piece.Team;
import java.util.List;

public class WinnerDto {

    private final Team team;

    public WinnerDto(Team team) {
        this.team = team;
    }

    public static WinnerDto of(Result result) {
        List<Team> winnerResult = result.getWinnerResult();
        return new WinnerDto(winnerResult.get(0));
    }

    public String getTeam() {
        return team.toString();
    }
}
