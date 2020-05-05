package wooteco.chess.domain.game.repository;

import java.util.Objects;

import org.springframework.data.annotation.Id;

import wooteco.chess.domain.board.BoardParser;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.GameFactory;

public class GameEntity {
	@Id
	private String id;
	private String state;
	private String turn;
	private String board;

	public GameEntity(String state, String turn, String board) {
		this.state = state;
		this.turn = turn;
		this.board = board;
	}

	public static GameEntity of(Game game) {
		return new GameEntity(game.getStateType(), game.getTurn().name(), BoardParser.parseString(game.getBoard()));
	}

	public void setIdIfIdIsNull(String id) {
		if (this.id == null) {
			this.id = id;
		}
	}

	public String getId() {
		return id;
	}

	public String getTurn() {
		return turn;
	}

	public String getBoard() {
		return board;
	}

	public void update(Game game) {
		this.state = game.getStateType();
		this.turn = game.getTurn().name();
		this.board = BoardParser.parseString(game.getBoard());
	}

	public Game toDomain() {
		return GameFactory.of(state, turn, board);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GameEntity that = (GameEntity)o;
		return Objects.equals(id, that.id) &&
			Objects.equals(state, that.state) &&
			Objects.equals(turn, that.turn) &&
			Objects.equals(board, that.board);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, state, turn, board);
	}
}
