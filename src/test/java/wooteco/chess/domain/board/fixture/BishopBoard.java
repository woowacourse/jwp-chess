package wooteco.chess.domain.board.fixture;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;

/*
 * E는 적, R는 아군 Rook을 나타냄
 *
 * 8  . . . . . . . .
 * 7  . . . . . . . .
 * 6  . . . . . . . .
 * 5  .  . . . . . .
 * 4  . . . . . . . .
 * 3  . . . E . . . .
 * 2  . . E b . . . .
 * 1  . b . . . . . .
 *    A B C D E F G H
 * */
public class BishopBoard {
	private List<Rank> ranks = new ArrayList<>();

	public Board create() {
		List<Piece> rank1 = new ArrayList<>();
		makePiece(rank1, "b1", Team.WHITE);

		List<Piece> rank2 = new ArrayList<>();
		makePiece(rank2, "c2", Team.BLACK);
		makePiece(rank2, "d2", Team.WHITE);

		List<Piece> rank3 = new ArrayList<>();
		makePiece(rank3, "d3", Team.BLACK);

		ranks.add(new Rank(rank1));
		ranks.add(new Rank(rank2));
		ranks.add(new Rank(rank3));
		createEmptyRank(5);

		return new Board(ranks);
	}

	private void makePiece(List<Piece> rank, String position, Team team) {
		rank.add(new Bishop(Position.of(position), team));
	}

	private void createEmptyRank(int emptyRankSize) {
		for (int i = 0; i < emptyRankSize; i++) {
			ranks.add(new Rank(new ArrayList<>()));
		}
	}
}
