package wooteco.chess.domain.game;

import static wooteco.chess.domain.piece.Team.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public abstract class Game {
	private static final String ILLEGAL_STATE_CHANGE_REQUEST_EXCEPTION_MESSAGE = "유효하지 않는 변경 요청입니다.";
	private static final Map<String, Function<Game, Game>> CHANGE_STATE_FUNCTIONS;
	private static final String START_REQUEST = "start";
	private static final String END_REQUEST = "end";

	static {
		CHANGE_STATE_FUNCTIONS = new HashMap<>();
		CHANGE_STATE_FUNCTIONS.put(START_REQUEST, Game::start);
		CHANGE_STATE_FUNCTIONS.put(END_REQUEST, Game::end);
	}

	protected final int id;
	protected final Board board;
	protected Team turn;

	public Game(Board board) {
		this(1, board, WHITE);
	}

	public Game(Board board, Team turn) {
		this(1, board, turn);
	}

	public Game(int id, Board board, Team turn) {
		this.id = id;
		this.board = Objects.requireNonNull(board);
		this.turn = Objects.requireNonNull(turn);
	}

	public abstract Game start();

	public abstract Game move(Position from, Position to);

	public abstract Result status();

	public abstract Game end();

	public abstract boolean isNotFinished();

	public Game changeState(String request) {
		if (!CHANGE_STATE_FUNCTIONS.containsKey(request)) {
			throw new IllegalArgumentException(ILLEGAL_STATE_CHANGE_REQUEST_EXCEPTION_MESSAGE);
		}
		Function<Game, Game> gameFunction = CHANGE_STATE_FUNCTIONS.get(request);
		return gameFunction.apply(this);
	}

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
