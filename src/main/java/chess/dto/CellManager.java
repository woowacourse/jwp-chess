package chess.dto;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.board.PositionFactory;
import chess.domain.piece.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CellManager {
    private static final char MIN_Y_POINT = '1';
    private static final char MAX_Y_POINT = '8';
    private static final char MIN_X_POINT = 'a';
    private static final char MAX_X_POINT = 'h';

    public List<Cell> createCells(ChessBoard chessBoard) {
        List<Cell> cells = new ArrayList<>();

        Map<Position, Piece> boardData = chessBoard.getBoard();

        for (char yPoint = MAX_Y_POINT; yPoint >= MIN_Y_POINT; yPoint--) {
            for (char xPoint = MIN_X_POINT; xPoint <= MAX_X_POINT; xPoint++) {
                Position position = PositionFactory.of(xPoint, yPoint);
                Piece piece = boardData.get(position);

                cells.add(Cell.from(position, piece));
            }
        }

        return cells;
    }
}
