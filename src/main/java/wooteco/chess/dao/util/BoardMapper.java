package wooteco.chess.dao.util;

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
	public static Board create(List<BoardDto> boardDtos) {
		Map<Position, Piece> board = new TreeMap<>();
		for (BoardDto boardDto : boardDtos) {
			board.put(
				Position.of(boardDto.getPiecePosition()),
				PieceFactory.of(boardDto.getPieceName(), boardDto.getPieceTeam(), boardDto.getPiecePosition())
			);
		}
		return new Board(board);
	}

	public static List<BoardDto> createMappers(Board board) {
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
