package chess.dto;

import chess.domain.piece.PieceColor;

import static chess.domain.GameStatus.FINISHED;
import static chess.domain.GameStatus.PLAYING;

public class GameStatusDto {

    private final String turn;
    private final String status;

    public GameStatusDto(String turn, String status) {
        this.turn = turn;
        this.status = status;
    }

    public static GameStatusDto from(PieceColor turnColor, boolean isPlaying) {
        if (isPlaying) {
            return new GameStatusDto(turnColor.getName(), PLAYING.getName());
        }
        return new GameStatusDto(turnColor.getName(), FINISHED.getName());
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
