package wooteco.chess.repository;

import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRule;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Table("piece")
public class PieceEntity {
    private String position;
    private String type;
    private Team team;

    public PieceEntity() {
    }

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