package domain.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.chess.domain.board.ChessBoard;
import wooteco.chess.domain.board.PositionFactory;
import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceColor;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.exception.AnotherTeamPieceException;
import wooteco.chess.exception.PieceNotFoundException;

public class ChessBoardTest {
	private static ChessBoard chessBoard;

	@BeforeAll
	static void setBoard() {
		chessBoard = new ChessBoard();
	}

	@Test
	@DisplayName("EmptyPiece가 아닌 체스말이 있는 위치값이 입력되면 해당 체스말을 반환해야 함")
	void inputPiecePositionThenReturnPiece() {
		Piece piece = chessBoard.findPiece(PositionFactory.of("a1"), PieceColor.WHITE);
		Assertions.assertThat(piece instanceof Rook).isTrue();

		piece = chessBoard.findPiece(PositionFactory.of("b8"), PieceColor.BLACK);
		Assertions.assertThat(piece instanceof Knight).isTrue();

		piece = chessBoard.findPiece(PositionFactory.of("c1"), PieceColor.WHITE);
		Assertions.assertThat(piece instanceof Bishop).isTrue();

		piece = chessBoard.findPiece(PositionFactory.of("d8"), PieceColor.BLACK);
		Assertions.assertThat(piece instanceof Queen).isTrue();

		piece = chessBoard.findPiece(PositionFactory.of("e1"), PieceColor.WHITE);
		Assertions.assertThat(piece instanceof King).isTrue();

		piece = chessBoard.findPiece(PositionFactory.of("c2"), PieceColor.WHITE);
		Assertions.assertThat(piece instanceof Pawn).isTrue();
	}

	@ParameterizedTest
	@DisplayName("체스말이 없는, 즉 EmptyPiece가 있는 위치값으로 체스말을 찾을 때 예외가 발생해야 함")
	@ValueSource(strings = {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
		"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
		"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
		"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"})
	void inputNoPiecePositionThenThrowException(String input) {
		Assertions.assertThatThrownBy(() -> chessBoard.findPiece(PositionFactory.of(input), PieceColor.NONE))
			.isInstanceOf(PieceNotFoundException.class)
			.hasMessage(String.format("위치(sourcePosition) %s에 움직일 수 있는 체스말이 없습니다.", input));
	}

	@ParameterizedTest
	@DisplayName("현재 팀의 색과 다른 색의 체스말을 찾을 때 예외가 발생해야 함")
	@ValueSource(strings = {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "a7", "b7", "c7", "d7", "e7", "f7", "g7",
		"h7"})
	void findDifferentPieceColorPositionThenThrowException(String input) {
		Assertions.assertThatThrownBy(() -> chessBoard.findPiece(PositionFactory.of(input), PieceColor.WHITE))
			.isInstanceOf(AnotherTeamPieceException.class)
			.hasMessage(String.format("위치(sourcePosition) %s의 말은 현재 차례인 %s의 말이 아니므로 움직일 수 없습니다.", input,
				this.chessBoard.getTeam().getName()));
	}
}
