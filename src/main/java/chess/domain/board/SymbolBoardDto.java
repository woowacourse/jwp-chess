package chess.domain.board;

import java.util.Map;

import chess.domain.chess.Chess;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class SymbolBoardDto {

    private static final int BOARD_SIZE = 8;

    private final String[][] board;

    private SymbolBoardDto(String[][] board) {
        this.board = board;
    }

    public static SymbolBoardDto from(Chess chess) {
        final Map<Position, Piece> pieceBoard = chess.getBoard()
                                                     .getBoard();
        final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
        for (Map.Entry<Position, Piece> entry : pieceBoard.entrySet()) {
            final int x = entry.getKey()
                               .getX();
            final int y = entry.getKey()
                               .getY();
            board[x][y] = entry.getValue()
                               .getSymbol();
        }

        return new SymbolBoardDto(board);
    }

    public String[][] getBoard() {
        return board;
    }
}
