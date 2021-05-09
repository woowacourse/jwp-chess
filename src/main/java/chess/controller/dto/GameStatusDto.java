package chess.controller.dto;

import chess.domain.game.Game;
import chess.domain.piece.Color;

public class GameStatusDto {
    private final BoardDto boardDto;
    private final double blackScore;
    private final double whiteScore;
    private final String turn;

    public GameStatusDto(Game game) {
        boardDto = new BoardDto(game.allBoard());
        blackScore = game.score(Color.BLACK);
        whiteScore = game.score(Color.WHITE);
        turn = game.currentPlayer()
                   .name();
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public String getTurn() {
        return turn;
    }
}
