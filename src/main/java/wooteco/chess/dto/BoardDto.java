package wooteco.chess.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.chess.domain.game.Board;
import wooteco.chess.domain.piece.Piece;

public class BoardDto {
    private List<String> board;

    public BoardDto(Board board) {
        this.board = board.getBoard()
            .values()
            .stream()
            .map(BoardDto::getSymbolByPiece)
            .collect(Collectors.toList());
    }

    private static String getSymbolByPiece(Piece piece) {
        if(piece.isBlack()) {
            return piece.symbol().toUpperCase();
        }
        return piece.symbol();
    }

    public List<String> getBoard() {
        return board;
    }

    public void setBoard(List<String> board) {
        this.board = board;
    }
}
