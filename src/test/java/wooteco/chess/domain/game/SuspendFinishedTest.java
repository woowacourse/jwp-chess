package wooteco.chess.domain.game;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.piece.Team.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

class SuspendFinishedTest {
	@DisplayName("게임 시작 하기 전에 게임을 종료하는 경우 승자는 없다.")
	@Test
	void getWinner_draw_when_game_state_is_ready_Test() {
		Game game = new SuspendFinished(new Board(), BLACK);
		Team actual = game.getWinner();
		assertThat(actual).isEqualTo(NONE);
	}

	@DisplayName("게임 중 체스를 종료할때, 총점이 더 높은 팀이 승리한다.")
	@ParameterizedTest
	@MethodSource("whitePieceAndWinningTeamSet")
	void getWinner_when_higher_score_team_test(Piece blackPiece, Piece whitePiece, Team expected) {
		Map<Position, Piece> pieces = new HashMap<>();
		pieces.put(Position.of("a1"), blackPiece);
		pieces.put(Position.of("a2"), whitePiece);
		Board board = new Board(pieces);
		Game game = new SuspendFinished(board, WHITE);
		Team actual = game.getWinner();
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> whitePieceAndWinningTeamSet() {
		return Stream.of(
			Arguments.of(new Bishop(BLACK), new Pawn(WHITE), BLACK),
			Arguments.of(new Bishop(BLACK), new Bishop(WHITE), NONE),
			Arguments.of(new Bishop(BLACK), new Rook(WHITE), WHITE)
		);
	}
}