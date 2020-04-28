package wooteco.chess.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;

public class ChessGame {
	@Id
	@Column("room_id")
	private String roomId;

	private Board board;

	private Team turn;
}
