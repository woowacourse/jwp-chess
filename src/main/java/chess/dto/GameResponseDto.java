package chess.dto;

import java.util.List;

public class GameResponseDto {

    private List<SquareDto> squares;
    private String turn;

    public GameResponseDto(List<SquareDto> squares, String turn) {
        this.squares = squares;
        this.turn = turn;
    }

    public List<SquareDto> getSquares() {
        return squares;
    }

    public void setSquares(List<SquareDto> squares) {
        this.squares = squares;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }
}
