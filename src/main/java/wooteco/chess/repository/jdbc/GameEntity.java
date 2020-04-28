package wooteco.chess.repository.jdbc;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.GameFactory;

@Table("game")
public class GameEntity {
	@Id
	private int id;
	private String state;
	private String turn;
	private String board;

	public GameEntity(String state, String turn, String board) {
		this.state = state;
		this.turn = turn;
		this.board = board;
	}

	public String getTurn() {
		return turn;
	}

	public String getBoard() {
		return board;
	}

	public void update(GameEntity gameEntity) {
		this.state = gameEntity.state;
		this.turn = gameEntity.turn;
		this.board = gameEntity.board;
	}

	public Game convert() {
		return GameFactory.of(state, turn, board);
	}
}
