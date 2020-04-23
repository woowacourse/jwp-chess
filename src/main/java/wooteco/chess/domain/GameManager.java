package wooteco.chess.domain;

import static wooteco.chess.domain.piece.Color.*;

import java.util.Map;
import java.util.Set;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class GameManager {
	private static final String TURN_MISS_MATCH_MESSAGE = "자신의 턴이 아닙니다.";
	private static final String NOT_MOVABLE_MESSAGE = "이동할 수 없는 위치입니다.";

	private Board board;
	private Color currentTurn;

	public GameManager(Board board) {
		this.board = board;
		this.currentTurn = WHITE;
	}

	public GameManager(Board board, Color turn) {
		this.board = board;
		this.currentTurn = turn;
	}

	public void move(Position targetPosition, Position destination) {
		validateMove(targetPosition, destination);

		board.movePiece(targetPosition, destination);
		nextTurn();
	}

	private void validateMove(Position targetPosition, Position destination) {
		Piece target = board.findPieceBy(targetPosition);
		validateTurn(target);
		validateMovablePosition(target, targetPosition, destination);
	}

	private void validateTurn(Piece target) {
		if (target.isNotSameColor(currentTurn)) {
			throw new IllegalArgumentException(TURN_MISS_MATCH_MESSAGE);
		}
	}

	private void validateMovablePosition(Piece target, Position targetPosition, Position destination) {
		Set<Position> movablePositions = target.findMovablePositions(targetPosition,
			board.getPieces());
		if (!movablePositions.contains(destination)) {
			throw new IllegalArgumentException(NOT_MOVABLE_MESSAGE);
		}
	}

	private void nextTurn() {
		currentTurn = currentTurn.reverse();
	}

	public Map<Color, Double> calculateEachScore() {
		ScoreRule scoreRule = new ScoreRule();
		return scoreRule.calculateScore(board);
	}

	public Color getCurrentTurn() {
		return currentTurn;
	}

	public boolean isKingAlive() {
		System.out.println("턴" + currentTurn.name());
		return board.isKingAliveOf(currentTurn);
	}

	public Board getBoard() {
		return board;
	}

	public void resetGame() {
		board.deleteAll();
		board = BoardFactory.create();
		currentTurn = WHITE;
	}
}
