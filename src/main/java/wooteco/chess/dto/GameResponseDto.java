package wooteco.chess.dto;

import java.util.List;

public class GameResponseDto {
    private Long roomId;
    private List<String> pieces;
    private String turn;
    private double whiteScore;
    private double blackScore;
    private String end;

    public GameResponseDto(Long roomId, List<String> pieces, String turn, double whiteScore, double blackScore, String end) {
        this.roomId = roomId;
        this.pieces = pieces;
        this.turn = turn;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
        this.end = end;
    }

    public Long getRoomId() {
        return roomId;
    }

    public List<String> getPieces() {
        return pieces;
    }

    public String getTurn() {
        return turn;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public String getEnd() {
        return end;
    }
}
