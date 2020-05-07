package wooteco.chess.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.board.ChessBoard;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.PositionFactory;
import wooteco.chess.domain.board.Xpoint;
import wooteco.chess.domain.board.Ypoint;
import wooteco.chess.domain.piece.Piece;

public class CellManager {
	public List<Cell> createCells(ChessBoard chessBoard) {
		List<Cell> cells = new ArrayList<>();

		Map<Position, Piece> boardData = chessBoard.getBoard();

		for (int yPoint = Ypoint.EIGHT.getValue(); yPoint >= Ypoint.ONE.getValue(); yPoint--) {
			for (int xPoint = Xpoint.A.getValue(); xPoint <= Xpoint.H.getValue(); xPoint++) {
				Position position = PositionFactory.of(xPoint, yPoint);
				Piece piece = boardData.get(position);

				cells.add(Cell.from(position, piece));
			}
		}

		return cells;
	}
}
