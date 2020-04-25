package wooteco.chess;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Status;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.state.BoardRepository;
import wooteco.chess.domain.state.ChessGameState;
import wooteco.chess.domain.state.Playing;
import wooteco.chess.domain.state.Score;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChessGame {
	private ChessGameState state;

	public ChessGame(ChessGameState state) {
		this.state = state;
	}

	public void start() {
		state = state.start();
	}

	public void move(Position source, Position target) {
		state = state.move(source, target);
	}

	public void end() {
		state = state.end();
	}

	public Board board() {
		return state.board();
	}

	public boolean isEnd() {
		return state.isEnd();
	}

	public Turn turn() {
		return state.turn();
	}

	public Status status() {
		Score white = state.score(Team.WHITE);
		Score black = state.score(Team.BLACK);
		return new Status(white, black);
	}

	public static ChessGame createGameByMoves(Map<Integer, List<String>> moves) {
		Objects.requireNonNull(moves);

		ChessGame game = new ChessGame(new Playing(BoardRepository.create(), new Turn(Team.WHITE)));
		for (List<String> value : moves.values()) {
			game.move(Position.of(value.get(0)), Position.of(value.get(1)));
		}
		return game;
	}
}
