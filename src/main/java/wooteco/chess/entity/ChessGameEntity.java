package wooteco.chess.entity;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.State;
import wooteco.chess.domain.game.state.StateFactory;
import wooteco.chess.dto.BoardDto;

@Table
public class ChessGameEntity {
	@Id
	private String id;
	private String state;
	private String board;
	private String turn;

	public ChessGameEntity() {
	}

	public ChessGameEntity(ChessGame chessGame) {
		update(chessGame);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (Objects.isNull(this.id)) {
			this.id = id;
		}
	}

	public String getState() {
		return state;
	}

	public String getBoard() {
		return board;
	}

	public String getTurn() {
		return turn;
	}

	public void update(ChessGame chessGame) {
		this.state = chessGame.getState().toString();
		this.board = String.join("", new BoardDto(chessGame.board()).getBoard());
		this.turn = String.valueOf(chessGame.turn());
	}

	public ChessGame toDomain() {
		State state = StateFactory.valueOf(this.state)
				.create(board, turn);
		return new ChessGame(state);
	}
}
