package wooteco.chess.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.position.Column;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;

public class BoardFactoryTest {
	private static Stream<Arguments> initRanks() {
		List<Rank> ranks = new ArrayList<>();

		List<Piece> first = new ArrayList<>();
		first.add(new Rook(Position.of("a1"), Team.WHITE));
		first.add(new Knight(Position.of("b1"), Team.WHITE));
		first.add(new Bishop(Position.of("c1"), Team.WHITE));
		first.add(new Queen(Position.of("d1"), Team.WHITE));
		first.add(new King(Position.of("e1"), Team.WHITE));
		first.add(new Bishop(Position.of("f1"), Team.WHITE));
		first.add(new Knight(Position.of("g1"), Team.WHITE));
		first.add(new Rook(Position.of("h1"), Team.WHITE));
		ranks.add(new Rank(first));

		List<Piece> second = new ArrayList<>();
		for (Column column : Column.values()) {
			second.add(new Pawn(Position.of(column.getColumnName() + "2"), Team.WHITE));
		}
		ranks.add(new Rank(second));

		for (int i = 0; i < 4; i++) {
			ranks.add(new Rank(new ArrayList<>()));
		}

		List<Piece> seventh = new ArrayList<>();
		for (Column column : Column.values()) {
			seventh.add(new Pawn(Position.of(column.getColumnName() + "7"), Team.BLACK));
		}
		ranks.add(new Rank(seventh));

		List<Piece> eighth = new ArrayList<>();
		eighth.add(new Rook(Position.of("a8"), Team.BLACK));
		eighth.add(new Knight(Position.of("b8"), Team.BLACK));
		eighth.add(new Bishop(Position.of("c8"), Team.BLACK));
		eighth.add(new Queen(Position.of("d8"), Team.BLACK));
		eighth.add(new King(Position.of("e8"), Team.BLACK));
		eighth.add(new Bishop(Position.of("f8"), Team.BLACK));
		eighth.add(new Knight(Position.of("g8"), Team.BLACK));
		eighth.add(new Rook(Position.of("h8"), Team.BLACK));
		ranks.add(new Rank(eighth));

		return Stream.of(Arguments.of(ranks));
	}

	@DisplayName("체스 보드판 생성")
	@Test
	void create_CreateInitialBoard() {
		assertThat(BoardFactory.create()).isInstanceOf(Board.class);
	}

	@DisplayName("각 말의 위치 확인")
	@MethodSource("initRanks")
	@ParameterizedTest
	void create_initializedBoard_True(List<Rank> expectedRanks) {
		List<Rank> ranks = BoardFactory.create().getRanks();
		for (int i = 0; i < expectedRanks.size(); i++) {
			assertThat(ranks.get(i)).isEqualTo(expectedRanks.get(i));
		}
	}
}
