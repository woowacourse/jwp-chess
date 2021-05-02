package chess.websocket.commander.dto;

import chess.controller.dto.PieceDto;
import chess.domain.TeamColor;
import java.util.List;

public class GameRoomResponseDto {

    private List<PieceDto> pieces;
    private boolean player;
    private TeamColor teamColor;
    private String whitePlayer;
    private String blackPlayer;

    public GameRoomResponseDto(List<PieceDto> pieces, boolean player, TeamColor teamColor,
        String whitePlayer, String blackPlayer) {
        this.pieces = pieces;
        this.player = player;
        this.teamColor = teamColor;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public void setPieces(List<PieceDto> pieces) {
        this.pieces = pieces;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }
}
