package dto;

import chess.domain.ChessGame;

public class ChessGameDto {
    private final TeamDto whiteTeam;
    private final TeamDto blackTeam;
    private final boolean isEnd;

    public ChessGameDto(ChessGame chessGame) {
        this.whiteTeam = new TeamDto(chessGame.getWhiteTeam());
        this.blackTeam = new TeamDto(chessGame.getBlackTeam());
        this.isEnd = chessGame.isEnd();
    }

    public TeamDto getBlackTeam() {
        return blackTeam;
    }

    public TeamDto getWhiteTeam() {
        return whiteTeam;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
