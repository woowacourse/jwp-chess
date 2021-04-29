package chess.dto;

import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.utils.Serializer;

public class ChessGameDto {

    private final long id;
    private final String turn;
    private final boolean finished;
    private final ChessBoardDto chessBoardDto;
    private final double blackScore;
    private final double whiteScore;
    private final String title;

    public ChessGameDto(ChessGame chessGame) {
        this.id = chessGame.getId();
        this.turn = chessGame.getTurn();
        this.finished = chessGame.isFinished();
        this.chessBoardDto = Serializer.chessGameAsDto(chessGame);
        this.blackScore = chessGame.getScore(Color.BLACK);
        this.whiteScore = chessGame.getScore(Color.WHITE);
        this.title = chessGame.getTitle();
    }

    public ChessGameDto(long id, String turn, boolean finished, ChessBoardDto chessBoardDto, double blackScore,
                        double whiteScore, String title) {
        this.id = id;
        this.turn = turn;
        this.finished = finished;
        this.chessBoardDto = chessBoardDto;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return finished;
    }

    public ChessBoardDto getChessBoardDto() {
        return chessBoardDto;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public String getTitle() {
        return title;
    }
}
