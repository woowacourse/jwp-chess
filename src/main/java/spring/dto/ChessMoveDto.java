package spring.dto;

import spring.chess.progress.Progress;
import spring.chess.team.Team;

public class ChessMoveDto {
    private final ChessGameScoresDto chessGameScoresDto;
    private final Progress progress;
    private final Team turn;

    public ChessMoveDto(ChessGameScoresDto chessGameScoresDto, Progress progress, Team turn) {
        this.chessGameScoresDto = chessGameScoresDto;
        this.progress = progress;
        this.turn = turn;
    }
}
