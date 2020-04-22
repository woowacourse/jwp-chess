package wooteco.chess.dao;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.Fixtures.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.Empty;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PiecesFactory;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Column;

public class BoardDAOTest {

	private BoardDAO boardDAO;

	@BeforeEach
	void setUp() {
		boardDAO = new BoardDAO();
	}

	@AfterEach
	void tearDown() {
		boardDAO.truncate();
	}

	@Test
	void create() {
		assertThat(new BoardDAO()).isInstanceOf(BoardDAO.class);
	}

	@Test
	void addPiece() {
		Piece piece = new King(A3, Team.WHITE);
		boardDAO.addPiece("1", piece);

		assertThat(boardDAO.findPieceBy("1", A3).getSymbol()).isEqualTo(piece.getSymbol());

	}

	@Test
	void findAll() {
		List<Piece> initial = new ArrayList<>(PiecesFactory.createInitial());
		for (Piece piece : initial) {
			boardDAO.addPiece("1" ,piece);
		}

		assertThat(boardDAO.findAll("1").size()).isEqualTo(64);
	}

	@Test
	void update() {
		boardDAO.addPiece("1", new Pawn(A2, Team.WHITE));
		boardDAO.addPiece("1", new Empty(A4));

		boardDAO.update("1", A2, A4);

		assertThat(boardDAO.findPieceBy("1", A2)).isInstanceOf(Empty.class);
		assertThat(boardDAO.findPieceBy("1", A4)).isInstanceOf(Pawn.class);
	}

	@Test
	void findByColumn() {
		List<Piece> initial = new ArrayList<>(PiecesFactory.createInitial());
		for (Piece piece : initial) {
			boardDAO.addPiece("1", piece);
		}

		assertThat(boardDAO.findGroupBy("1", Column.A).size()).isEqualTo(8);
	}
}
