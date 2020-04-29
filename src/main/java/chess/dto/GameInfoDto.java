package chess.dto;

import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Team;
import java.util.Map;

public class GameInfoDto {

    private final Team turn;
    private final Map<Team, String> userNames;
    private final TeamScore teamScores;

    public GameInfoDto(Team turn, Map<Team, String> userNames, TeamScore teamScores) {
        this.turn = turn;
        this.userNames = userNames;
        this.teamScores = teamScores;
    }

    public Team getTurn() {
        return turn;
    }

    public Map<Team, String> getUserNames() {
        return userNames;
    }

    public TeamScore getTeamScores() {
        return teamScores;
    }
}
