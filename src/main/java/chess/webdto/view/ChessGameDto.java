package chess.webdto.view;

import chess.domain.ChessGame;
import chess.webdto.converter.TeamConstants;

public class ChessGameDto {
    private final TeamPiecesDto piecePositionByTeam;
    private final String currentTurnTeam;
    private final ScoreDto teamScore;
    private final boolean isPlaying;

    public ChessGameDto(ChessGame chessGame){
        this.piecePositionByTeam = new TeamPiecesDto(chessGame);
        this.currentTurnTeam = TeamConstants.convert(chessGame.isWhiteTeamTurn());
        this.teamScore = new ScoreDto(chessGame.calculateWhiteTeamScore(), chessGame.calculateBlackTeamScore());
        this.isPlaying = chessGame.isPlaying();
    }


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
