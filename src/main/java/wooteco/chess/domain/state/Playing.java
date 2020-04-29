package wooteco.chess.domain.state;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.position.Position;

public class Playing extends Ready {
	protected final Board board;
	private Turn turn;

	public Playing(Board board, Turn turn) {
		this.board = board;
		this.turn = turn;
	}

	@Override
	public boolean isEnd() {
		return false;
	}

	@Override
	public ChessGameState start() {
		throw new UnsupportedOperationException("게임이 이미 시작되었습니다.");
	}

	@Override
	public ChessGameState move(Position source, Position target) {
		checkKingDead();
		board.move(source, target, turn);
		if (board.isKingDead()) {
			return new Finish(board);
		}
		turn = turn.switchTurn();
		return this;
	}

	private void checkKingDead() {
		if (board.isKingDead()) {
			throw new IllegalArgumentException("왕이 죽어 게임을 진행할 수 없습니다.");
		}
	}

	@Override
	public ChessGameState end() {
		return new Finish(board);
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
		return turn;
	}
}
