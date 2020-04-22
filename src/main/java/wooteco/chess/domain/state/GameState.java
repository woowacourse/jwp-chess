package wooteco.chess.domain.state;

import static wooteco.chess.domain.piece.Team.*;

import java.util.Objects;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public abstract class GameState {
	protected final int id;
	protected final Board board;
	protected Team turn;

	public GameState(Board board) {
		this(1, board, WHITE);
	}

	public GameState(Board board, Team turn) {
		this(1, board, turn);
	}

	public GameState(int id, Board board, Team turn) {
		this.id = id;
		this.board = Objects.requireNonNull(board);
		this.turn = Objects.requireNonNull(turn);
	}

	public abstract GameState start();

	public abstract GameState move(Position from, Position to);

	public abstract Result status();

	public abstract GameState end();

	public abstract boolean isNotFinished();

	public Team getWinner() {
		throw new UnsupportedOperationException("게임이 끝났을때만 승자를 구할수 있어요.");
	}

	public Board getBoard() {
		return board;
	}

	public Team getTurn() {
		return turn;
	}

	public int getId() {
		return id;
	}

	abstract public String getStateType();
}
