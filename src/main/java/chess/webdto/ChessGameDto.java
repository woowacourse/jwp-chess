package chess.webdto;

import java.util.Map;

public class ChessGameDto {
    private final TeamPiecesDto piecePositionByTeam;
    private final String currentTurnTeam;
    private final ScoreDto teamScore;
    private final boolean isPlaying;

    public ChessGameDto(final TeamPiecesDto piecePositionByTeam, final String currentTurnTeam,
                        final ScoreDto teamScore, final boolean isPlaying) {
        this.piecePositionByTeam = piecePositionByTeam;
        this.currentTurnTeam = currentTurnTeam;
        this.teamScore = teamScore;
        this.isPlaying = isPlaying;
    }

    public TeamPiecesDto getPiecePositionByTeam() {
        return piecePositionByTeam;
    }

    public String getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    public ScoreDto getTeamScore() {
        return teamScore;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }
}
