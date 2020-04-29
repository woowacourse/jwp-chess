package wooteco.chess.db;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import wooteco.chess.db.entity.BoardEntity;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.position.Position;

public class BoardMapper {
	public static Board create(List<BoardEntity> boardEntities) {
		Map<Position, Piece> board = new TreeMap<>();
		for (BoardEntity boardEntity : boardEntities) {
			board.put(
				Position.of(boardEntity.getPiecePosition()),
				PieceFactory.of(boardEntity.getPieceName(), boardEntity.getPieceTeam(), boardEntity.getPiecePosition())
			);
		}
		return new Board(board);
	}

	public static List<BoardEntity> createMappers(Board board) {
		return board.getBoard().entrySet()
			.stream()
			.map(entry -> {
				Position key = entry.getKey();
				Piece value = entry.getValue();
				return new BoardEntity(value.getSymbol(), value.getTeam().name(), key.getName());
			})
			.collect(Collectors.toList());
	}
}
