package chess.webdto;

import java.util.Map;

public class ChessGameDto {
    private final Map<String, Map<String, String>> piecePositionByTeam;
    private final String currentTurnTeam;
    private final Map<String, Double> teamScore;
    private final boolean isPlaying;
    private final int roomNumber;

    public ChessGameDto(final Map<String, Map<String, String>> piecePositionByTeam, final String currentTurnTeam,
                        final Map<String, Double> teamScore, final boolean isPlaying, final int roomNumber) {
        this.piecePositionByTeam = piecePositionByTeam;
        this.currentTurnTeam = currentTurnTeam;
        this.teamScore = teamScore;
        this.isPlaying = isPlaying;
        this.roomNumber = roomNumber;
    }

    public Map<String, Map<String, String>> getPiecePositionByTeam() {
        return piecePositionByTeam;
    }

    public String getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    public Map<String, Double> getTeamScore() {
        return teamScore;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
