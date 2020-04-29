package wooteco.chess.dto;

import wooteco.chess.domain.ChessManager;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameResponse {
    private List<TileDto> tiles;
    private Team currentTeam;
    private double currentTeamScore;

    public GameResponse(ChessManager chessManager) {
        this.tiles = new TilesDto(chessManager.getBoard()).get();
        this.currentTeam = chessManager.getCurrentTeam();
        this.currentTeamScore = chessManager.calculateScore();
    }

    public Map<String, Object> get() {
        Map<String, Object> model = new HashMap<>();
        model.put("chessPieces", tiles);
        model.put("currentTeam", currentTeam);
        model.put("currentTeamScore", currentTeamScore);

        return model;
    }
}
