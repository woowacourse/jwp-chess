package chess.controller.dto;

public class GameDto {
    private final String turn;
    private final String board;

    public GameDto(final String turn, final String board) {
        this.board = board;
        this.turn = turn;
    }

    public String getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }
}
