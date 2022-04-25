package chess.dto;

import chess.entity.BoardEntity;

public class BoardDto {

    private Long id;
    private String position;
    private String piece;

    private BoardDto(final Long id, final String position, final String piece) {
        this.id = id;
        this.position = position;
        this.piece = piece;
    }

    public static BoardDto of(final BoardEntity board) {
        return new BoardDto(board.getId(), board.getPosition(), board.getPiece());
    }

    public Long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }
}
