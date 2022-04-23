package chess.model.piece;

import java.util.Arrays;
import java.util.List;

public enum Team {

    WHITE,
    BLACK,
    EMPTY
    ;

    public static List<Team> getPlayerTeams() {
        return List.of(WHITE, BLACK);
    }

    public boolean isBlack() {
        return this.equals(BLACK);
    }

    public static Team findTeam(String name) {
        return Arrays.stream(values())
                .filter(team -> team.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알맞은 팀이 아닙니다: " + name));
    }

    public boolean isProperTurn(Team team) {
        if (this.equals(WHITE)) {
            return team.equals(this);
        }
        return team.equals(BLACK);
    }

    public Team oppositeTeam() {
        if (this.equals(WHITE)) {
            return BLACK;
        }
        return WHITE;
    }
}
