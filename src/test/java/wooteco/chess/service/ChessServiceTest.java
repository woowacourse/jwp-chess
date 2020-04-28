package wooteco.chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.dao.GameDao;
import wooteco.chess.dao.PieceDao;
import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MoveRequestDto;

class ChessServiceTest {
	private GameDao gameDao;
	private PieceDao pieceDao;
	private ChessService chessService;

	@BeforeEach
	void setUp() {
		gameDao = GameDao.getInstance();
		pieceDao = PieceDao.getInstance();
		chessService = new ChessService(pieceDao, gameDao);
	}

	@AfterEach
	void tearDown() {
		pieceDao.deleteAll();
		gameDao.deleteAll();
	}

	@DisplayName("새로운 게임을 생성한다.")
	@Test
	void createGame() {
		BoardDto boardDto = chessService.createGame();
		assertThat(gameDao.findById(boardDto.getGameId())).isNotNull();
	}

	@DisplayName("생성된 게임을 불러온다.")
	@Test
	void load() {
		//given
		BoardDto boardDto = chessService.createGame();

		//when
		BoardDto load = chessService.load(boardDto.getGameId());

		//then
		assertThat(load.getPiece()).containsAll(boardDto.getPiece());
	}

	@DisplayName("체스말을 이동시킨다.")
	@Test
	void move() {
		//given
		BoardDto game = chessService.createGame();
		String command = "move a2 a4";
		MoveRequestDto moveRequestDto = new MoveRequestDto(game.getGameId(), command);

		//when
		chessService.move(moveRequestDto);

		//then
		List<Piece> pieces = pieceDao.findAllByGameId(game.getGameId());
		Rank rank = new Rank(pieces);
		Piece a4 = rank.findPiece(Position.of("a4"))
			.orElseThrow(IllegalArgumentException::new);
		boolean a2Exist = rank.findPiece(Position.of("a2")).isPresent();

		assertThat(a4).isEqualTo(new Pawn(Position.of("a4"), Team.WHITE));
		assertThat(a2Exist).isFalse();
	}
}