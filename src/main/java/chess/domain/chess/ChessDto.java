package chess.domain.chess;

import chess.domain.board.BoardDto;

public class ChessDto {

    private final BoardDto boardDto;
    private final String status;
    private final String turn;

    public ChessDto(Chess chess) {
        this(BoardDto.from(chess), chess.status(), chess.color());
    }

    public ChessDto(BoardDto boardDto, String status, String turn) {
        this.boardDto = boardDto;
        this.status = status;
        this.turn = turn;
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }

    public String getStatus() {
        return status;
    }

    public String getTurn() {
        return turn;
    }
}
