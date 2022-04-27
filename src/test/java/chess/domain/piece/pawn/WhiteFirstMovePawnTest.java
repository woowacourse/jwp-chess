package chess.domain.piece.pawn;

import static chess.domain.Color.BLACK;
import static chess.domain.Color.WHITE;
import static chess.domain.piece.Piece.createBlackPiece;
import static chess.domain.piece.Piece.createWhitePiece;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.ChessBoard;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceRule;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class WhiteFirstMovePawnTest {

	private PieceRule pawn;
	private Position source;

	@BeforeEach
	void setUp() {
		source = Position.of('b', '2');
		pawn = new Pawn(WHITE);
	}

	@ParameterizedTest
	@CsvSource(value = {"b,2", "b,1", "a,1", "a,2"})
	@DisplayName("폰의 빈곳 전진 가능 여부 확인")
	void isMovableToEmptyPosition(char col, char row) {
		Position target = Position.of(col, row);
		ChessBoard chessBoard = new ChessBoard(Map.of(source, createWhitePiece(new Pawn(WHITE))));

		assertThatThrownBy(() -> pawn.move(source, target, chessBoard))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("움직일 수 없는 곳입니다.");
	}

	@ParameterizedTest
	@MethodSource("cannotMoveToPiecePosition")
	@DisplayName("기물이 가로막을 경우의 전진 불가능")
	void cannotMoveToPiecePosition(Position target, Piece piece) {
		ChessBoard chessBoard = new ChessBoard(Map.of(
				source, createWhitePiece(new Pawn(WHITE)),
				target, piece));

		assertThatThrownBy(() -> pawn.move(source, target, chessBoard))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("움직일 수 없는 곳입니다.");
	}

	private static Stream<Arguments> cannotMoveToPiecePosition() {
		return Stream.of(
				Arguments.of(Position.of('b', '3'), createWhitePiece(new Pawn(WHITE))),
				Arguments.of(Position.of('b', '4'), createWhitePiece(new Pawn(WHITE))),
				Arguments.of(Position.of('b', '3'), createBlackPiece(new Pawn(BLACK))),
				Arguments.of(Position.of('b', '4'), createBlackPiece(new Pawn(BLACK)))
		);
	}
}