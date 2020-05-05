package wooteco.chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.repository.GameRepository;

@SpringBootTest
class ChessServiceTest {
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private ChessService chessService;

	@AfterEach
	void tearDown() {
		gameRepository.deleteAll();
	}

	@DisplayName("새로운 게임을 생성한다.")
	@Test
	void createGame() {
		BoardDto boardDto = chessService.createGame();

		boolean present = gameRepository.findById(boardDto.getGameId()).isPresent();

		assertThat(present).isTrue();
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
		List<Piece> pieces = gameRepository.findById(game.getGameId())
			.orElseThrow(NoSuchElementException::new)
			.getChessGame()
			.getPieces();

		Rank rank = new Rank(pieces);
		Piece a4 = rank.findPiece(Position.of("a4"))
			.orElseThrow(IllegalArgumentException::new);
		boolean a2Exist = rank.findPiece(Position.of("a2")).isPresent();

		assertThat(a4).isEqualTo(new Pawn(Position.of("a4"), Team.WHITE));
		assertThat(a2Exist).isFalse();
	}
}