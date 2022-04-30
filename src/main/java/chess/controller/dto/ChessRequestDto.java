package chess.controller.dto;

import chess.domain.piece.PieceColor;
import chess.service.dto.GameStatusDto;

public class ChessRequestDto {

    private static final String DEFAULT_TURN = PieceColor.WHITE.getName();
    private static final String DEFAULT_STATUS = GameStatusDto.PLAYING.getName();

    private final String title;
    private final String password;

    public ChessRequestDto(final String title, final String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public String getTurn() {
        return DEFAULT_TURN;
    }

    public String getStatus() {
        return DEFAULT_STATUS;
    }
}
