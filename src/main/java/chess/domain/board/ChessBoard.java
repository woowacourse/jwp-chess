package chess.domain.board;

import static chess.domain.piece.Pawn.*;
import static chess.util.NullValidator.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import chess.domain.GameResult;
import chess.domain.command.MoveCommand;
import chess.domain.piece.EmptyPiece;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.exception.AnotherTeamPieceException;
import chess.exception.NotMovableException;
import chess.exception.OtherPieceInPathException;
import chess.exception.PawnNotAttackableException;
import chess.exception.PieceNotFoundException;
import chess.exception.SameTeamPieceException;

public class ChessBoard {
	private static final int ONLY_ONE_PAWN_IN_X_POINT = 1;
	private static final int ONE_STEP_RANGE = 1;
	private static final String BLACK_KING_MARK = "K";
	private static final String WHITE_KING_MARK = "k";

	private Map<Position, Piece> board;
	private PieceColor team;

	public ChessBoard(Map<Position, Piece> board, PieceColor team) {
		this.board = board;
		this.team = team;
	}

	public ChessBoard() {
		this(BoardFactory.createBoard(), PieceColor.WHITE);
	}

	public Piece findPiece(Position sourcePosition, PieceColor team) {
		validateNull(sourcePosition);

		Piece sourcePiece = board.get(sourcePosition);
		if (sourcePiece.isNone()) {
			throw new PieceNotFoundException(String.format("위치(sourcePosition) %s에 움직일 수 있는 체스말이 없습니다.",
				sourcePosition.getName()));
		}
		if (!sourcePiece.isSameColor(team)) {
			throw new AnotherTeamPieceException(String.format("위치(sourcePosition) %s의 말은 현재 차례인 %s의 말이 아니므로 움직일 수 " +
					"없습니다.", sourcePosition.getName(),
				team.getName()));
		}
		return board.get(sourcePosition);
	}

	private void checkPath(Piece piece, Position targetPosition) {
		if (piece.isPawn()) {
			checkPawnPath(piece, targetPosition);
			return;
		}

		List<Position> path = piece.getPathTo(targetPosition);

		if (piece.hasLongRangePieceStrategy()) {
			if (havePieceBeforeTargetPosition(path)) {
				throw new OtherPieceInPathException(String.format("이동 경로 중에 다른 체스말이 있기 때문에 지정한 위치(targetPosition) %s" +
						"(으)로 이동할 수 없습니다.",
					targetPosition.getName()));
			}
		}

		if (cannotMoveToTargetPosition(piece, targetPosition)) {
			throw new SameTeamPieceException(String.format("지정한 위치(targetPosition) %s에 같은 색의 체스말이 있기 때문에 이동할 수 없습니다."
				, targetPosition.getName()));
		}
	}

	private boolean havePieceBeforeTargetPosition(List<Position> path) {
		if (path.size() == ONE_STEP_RANGE) {
			return false;
		}

		path.remove(path.size() - 1);
		return havePieceIn(path);
	}

	private boolean cannotMoveToTargetPosition(Piece piece, Position targetPosition) {
		Piece targetPiece = board.get(targetPosition);
		return piece.isSameColor(targetPiece);
	}

	private void checkPawnPath(Piece piece, Position targetPosition) {
		Pawn pawn = (Pawn)piece;

		List<Position> path = piece.getPathTo(targetPosition);
		if ((pawn.getDirection(targetPosition).isSouth() || pawn.getDirection(targetPosition).isNorth()) && havePieceIn(
			path)) {
			throw new OtherPieceInPathException(String.format("이동 경로 중에 다른 체스말이 있기 때문에 지정한 위치(targetPosition) %s(으)로 " +
				"이동할 수 없습니다.", targetPosition.getName()));
		}
		if (!pawn.getDirection(targetPosition).isSouth() && !pawn.getDirection(targetPosition).isNorth()
			&& cannotMovePawnToTargetPosition(piece, targetPosition)) {
			throw new PawnNotAttackableException(String.format("지정한 위치(targetPosition) %s에 다른 색의 체스말이 없기 때문에 이동할 수 " +
				"없습니다.", targetPosition.getName()));
		}
	}

	private boolean havePieceIn(List<Position> path) {
		return !path.stream()
			.map(board::get)
			.allMatch(Piece::isNone);
	}

	private boolean cannotMovePawnToTargetPosition(Piece piece, Position targetPosition) {
		Piece targetPiece = board.get(targetPosition);
		if (targetPiece.isNone()) {
			return true;
		}
		return piece.isSameColor(targetPiece);
	}

	public void move(MoveCommand moveCommand) {
		validateNull(moveCommand);
		if (isGameOver()) {
			throw new NotMovableException("게임이 종료되서 움직일 수 없습니다.");
		}

		Piece piece = findPiece(moveCommand.getSourcePosition(), team);
		Position targetPosition = moveCommand.getTargetPosition();
		checkPath(piece, targetPosition);

		board.put(piece.getPosition(), new EmptyPiece(PieceColor.NONE, piece.getPosition()));
		board.put(targetPosition, piece);
		piece.changeTo(targetPosition);
		team = team.change();
	}

	private boolean isBlackKingKilled() {
		return board.values().stream()
			.map(Piece::getName)
			.noneMatch(BLACK_KING_MARK::equals);
	}

	private boolean isWhiteKingKilled() {
		return board.values().stream()
			.map(Piece::getName)
			.noneMatch(WHITE_KING_MARK::equals);
	}

	private double calculateAlivePieceScoreSumBy(PieceColor pieceColor) {
		double scoreSum = board.values().stream()
			.filter(piece -> piece.isNotPawn() && piece.isSameColor(pieceColor))
			.mapToDouble(Piece::getScore)
			.sum();

		return scoreSum + calculateAlivePawnScoreSumBy(pieceColor);
	}

	private double calculateAlivePawnScoreSumBy(PieceColor pieceColor) {
		double scoreSum = 0;

		for (Xpoint xpoint : Xpoint.values()) {
			int pawnInXPointCount = 0;
			for (Ypoint ypoint : Ypoint.values()) {
				Piece piece = board.get(PositionFactory.of(xpoint.getValue(), ypoint.getValue()));
				pawnInXPointCount = calculatePawnInXPointCount(pieceColor, pawnInXPointCount, piece);
			}
			scoreSum += calculatePawnScoreSumInXPoint(pawnInXPointCount);
		}

		return scoreSum;
	}

	private int calculatePawnInXPointCount(PieceColor pieceColor, int pawnInXPointCount, Piece piece) {
		if (piece.isPawn() && piece.isSameColor(pieceColor)) {
			pawnInXPointCount++;
		}
		return pawnInXPointCount;
	}

	private double calculatePawnScoreSumInXPoint(int pawnInXPointCount) {
		if (ONLY_ONE_PAWN_IN_X_POINT < pawnInXPointCount) {
			return pawnInXPointCount * PAWN_HALF_SCORE;
		}
		return pawnInXPointCount * PAWN_SCORE;
	}

	public GameResult createGameResult() {
		return new GameResult(isBlackKingKilled(), isWhiteKingKilled(), calculateAlivePieceScoreSumBy(PieceColor.BLACK),
			calculateAlivePieceScoreSumBy(PieceColor.WHITE));
	}

	public boolean isGameOver() {
		return isWhiteKingKilled() || isBlackKingKilled();
	}

	public PieceColor getTeam() {
		return team;
	}

	public Map<Position, Piece> getBoard() {
		return Collections.unmodifiableMap(board);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChessBoard that = (ChessBoard)o;
		return Objects.equals(board, that.board) &&
			team == that.team;
	}

	@Override
	public int hashCode() {
		return Objects.hash(board, team);
	}
}
