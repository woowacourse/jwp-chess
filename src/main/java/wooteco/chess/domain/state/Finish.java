package wooteco.chess.domain.state;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.position.Position;

public class Finish implements ChessGameState {
	private final Board board;

	public Finish(Board board) {
		this.board = board;
	}

	@Override
	public boolean isEnd() {
		return true;
	}

	@Override
	public ChessGameState start() {
		throw new UnsupportedOperationException("게임이 종료되었습니다.");
	}

	@Override
	public ChessGameState move(Position source, Position target) {
		throw new UnsupportedOperationException("게임이 종료되었습니다.");
	}

	@Override
	public ChessGameState end() {
		throw new UnsupportedOperationException("게임이 이미 종료되었습니다.");
	}

	@Override
	public Board board() {
		return board;
	}

	@Override
	public Score score(Team team) {
		return Score.calculate(board.findPiecesByTeam(team));
	}

	@Override
	public Turn turn() {
		throw new UnsupportedOperationException();
	}
}
