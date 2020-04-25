package wooteco.chess.dto;

import wooteco.chess.domain.ChessManager;
import wooteco.chess.domain.piece.Team;

import java.util.List;

public class GameResponse {
    private List<TileDto> tiles;
    private Team currentTeam;
    private double currentTeamScore;

    public GameResponse(ChessManager chessManager) {
        this.tiles = new TilesDto(chessManager.getBoard()).get();
        this.currentTeam = chessManager.getCurrentTeam();
        this.currentTeamScore = chessManager.calculateScore();
    }

    public List<TileDto> getTiles() {
        return tiles;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public double getCurrentTeamScore() {
        return currentTeamScore;
    }
}
