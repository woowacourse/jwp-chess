package wooteco.chess.dto.response;

import wooteco.chess.domain.ChessManager;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.TileDto;
import wooteco.chess.dto.TilesDto;

import java.util.List;

public class MoveResponse {
    private List<TileDto> chessPieces;
    private Team currentTeam;
    private double currentTeamScore;
    private Team winner;
    private Long roomId;
    private String roomName;

    public MoveResponse(ChessManager chessManager, Long roomId, String roomName) {
        this.chessPieces = new TilesDto(chessManager.getBoard()).get();
        this.currentTeam = chessManager.getCurrentTeam();
        this.currentTeamScore = chessManager.calculateScore();
        this.winner = chessManager.getWinner();
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public List<TileDto> getChessPieces() {
        return chessPieces;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public double getCurrentTeamScore() {
        return currentTeamScore;
    }

    public Team getWinner() {
        return winner;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
