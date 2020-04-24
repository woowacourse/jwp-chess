package wooteco.chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.PieceDto;

class PieceDaoTest {
	private GameDao gameDao = GameDao.getInstance();
	private PieceDao pieceDao = PieceDao.getInstance();

	@AfterEach
	void tearDown() {
		pieceDao.deleteAll();
		gameDao.deleteAll();
	}

	@DisplayName("save 기능 테스트")
	@Test
	void save() {
		//given
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);
		String position = "a5";
		Long gameId = gameDto.getId();
		PieceDto pieceDto = new PieceDto(gameId, "r", "white", "a5");

		//when
		pieceDao.save(pieceDto);
		Optional<PieceDto> savedPieceDto = pieceDao.findByGameIdAndPosition(gameId, position);

		//then
		assertThat(savedPieceDto.isPresent()).isTrue();
	}

	@DisplayName("update 기능 테스트")
	@Test
	void update() {
		//given
		String originalPosition = "a5";
		String newPosition = "a6";
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);
		Long gameId = gameDto.getId();
		PieceDto pieceDto = new PieceDto(gameId, "r", "white", originalPosition);
		pieceDao.save(pieceDto);
		PieceDto savedPieceDto = pieceDao.findByGameIdAndPosition(gameId, originalPosition).orElseThrow(NoSuchElementException::new);

		//when
		pieceDao.update(savedPieceDto.getId(), newPosition);

		//then
		PieceDto actual = pieceDao.findByGameIdAndPosition(gameId, newPosition).orElseThrow(NoSuchElementException::new);
		assertThat(actual.getPosition()).isEqualTo(newPosition);
	}

	@DisplayName("게임 아이디에 해당하는 모든 피스를 찾아온다")
	@Test
	void findAllByGameId() {
		//given
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);

		PieceDto pieceDto1 = new PieceDto(gameDto.getId(), "r", "white", "a1");
		PieceDto pieceDto2 = new PieceDto(gameDto.getId(), "r", "white", "a2");
		pieceDao.save(pieceDto1);
		pieceDao.save(pieceDto2);

		//when
		List<Piece> pieces = pieceDao.findAllByGameId(gameDto.getId());

		//then
		assertThat(pieces).hasSize(2);
	}
}
