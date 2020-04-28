package wooteco.chess.domain.player;

import org.springframework.data.relational.core.mapping.Table;

@Table("team_table")
public enum Team {

    WHITE,
    BLACK;

    public Team toggle() {
        if (this == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    public boolean isSameTeam(Team team) {
        return this == team;
    }

    public boolean isWhite() {
        return this == WHITE;
    }
}
