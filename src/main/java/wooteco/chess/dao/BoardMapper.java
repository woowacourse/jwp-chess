package wooteco.chess.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.BoardDto;

public class BoardMapper {
	public static Board create(List<BoardDto> mappers) {
		Map<Position, Piece> board = new TreeMap<>();
		for (BoardDto mapper : mappers) {
			board.put(Position.of(mapper.piecePosition()),
				PieceFactory.of(mapper.pieceName(), mapper.pieceTeam(), mapper.piecePosition()));
		}
		return new Board(board);
	}

	public static List<BoardDto> createMappers(Board board) {
		List<Object> pieces = new ArrayList<>();
		return board.getBoard().entrySet()
			.stream()
			.map(entry -> {
				Position key = entry.getKey();
				Piece value = entry.getValue();
				return new BoardDto(value.getSymbol(), value.getTeam().name(), key.getName());
			})
			.collect(Collectors.toList());
	}
}
