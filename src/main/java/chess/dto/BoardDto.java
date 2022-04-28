package chess.dto;

import chess.domain.Team;

import java.util.List;

public class BoardDto {

    private final List<PieceDto> pieces;
    private final Team team;
    private final Long id;

    public BoardDto(List<PieceDto> pieces, Team team, Long id) {
        this.pieces = pieces;
        this.team = team;
        this.id = id;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public Team getTeam() {
        return team;
    }

    public Long getId() {
        return id;
    }
}
