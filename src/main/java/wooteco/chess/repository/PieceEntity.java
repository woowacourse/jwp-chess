package wooteco.chess.repository;

import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Team;

@Table("piece")
public class PieceEntity {

    private String position;
    private String type;
    private Team team;

    public PieceEntity(String position, String type, Team team) {
        this.position = position;
        this.type = type;
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public Team getTeam() {
        return team;
    }
}