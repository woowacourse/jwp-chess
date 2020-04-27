package wooteco.chess.dao.util;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDto;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BoardMapper {
	public static Board convertToBoard(List<PieceDto> pieceDtos) {
		Map<Position, Piece> board = new TreeMap<>();
		for (PieceDto pieceDto : pieceDtos) {
			board.put(
				pieceDto.getPosition(),
				PieceFactory.of(pieceDto.getSymbol(), pieceDto.getTeam(), pieceDto.getPosition().getName())
			);
		}
		return new Board(board);
	}

	public static List<PieceDto> convertToPieceDto(Board board) {
		return board.getBoard().entrySet()
			.stream()
			.map(entry -> {
				Position position = entry.getKey();
				Piece piece = entry.getValue();
				return new PieceDto(position, piece.getTeam().name(), piece.getSymbol());
			})
			.collect(Collectors.toList());
	}
}
