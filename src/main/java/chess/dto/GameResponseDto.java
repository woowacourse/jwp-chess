package chess.dto;

import chess.domain.Game;
import chess.domain.board.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameResponseDto {

    private List<SquareDto> squares;
    private String turn;

    public static GameResponseDto of(Game game) {
        return new GameResponseDto(squareDtos(game), game.turnColor().getName());
    }

    private static List<SquareDto> squareDtos(Game game) {
        Board board = game.getBoard();
        return board.positions()
            .stream()
            .map(position -> new SquareDto(position.toString(),
                board.pieceAtPosition(position).toString()))
            .collect(Collectors.toList());
    }

    private GameResponseDto(List<SquareDto> squares, String turn) {
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
