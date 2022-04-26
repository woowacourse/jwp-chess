package chess.service.dto;

import chess.domain.piece.PieceColor;

import static chess.service.dto.GameStatusDto.FINISHED;
import static chess.service.dto.GameStatusDto.PLAYING;

public class GameDto {

    private final String turn;
    private final String status;

    private GameDto(String turn, String status) {
        this.turn = turn;
        this.status = status;
    }

    public static GameDto of(PieceColor turnColor, boolean isPlaying) {
        if (isPlaying) {
            return GameDto.of(turnColor.getName(), PLAYING.getName());
        }
        return GameDto.of(turnColor.getName(), FINISHED.getName());
    }

    public static GameDto of(String turn, String status) {
        return new GameDto(turn, status);
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
