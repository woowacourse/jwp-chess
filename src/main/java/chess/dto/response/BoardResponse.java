package chess.dto.response;

import chess.domain.Team;

import java.util.List;

public class BoardResponse {

    private final List<PieceResponse> pieces;
    private final Team team;
    private final Long id;

    public BoardResponse(List<PieceResponse> pieces, Team team, Long id) {
        this.pieces = pieces;
        this.team = team;
        this.id = id;
    }

    public List<PieceResponse> getPieces() {
        return pieces;
    }

    public Team getTeam() {
        return team;
    }

    public Long getId() {
        return id;
    }
}
