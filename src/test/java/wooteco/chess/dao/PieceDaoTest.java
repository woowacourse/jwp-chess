package wooteco.chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

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
	void tearDown() throws SQLException {
		pieceDao.deleteAll();
		gameDao.deleteAll();
	}

	@DisplayName("save 기능 테스트")
	@Test
	void save() throws SQLException {
		//given
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);
		PieceDto pieceDto = new PieceDto(gameDto.getId(), "r", "white", "a5");

		//when
		pieceDto = pieceDao.save(pieceDto);

		//then
		assertThat(pieceDto.getId()).isNotNull();
	}

	@DisplayName("update 기능 테스트")
	@Test
	void update() throws SQLException {
		//given
		String newPosition = "a6";
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);
		PieceDto pieceDto = new PieceDto(gameDto.getId(), "r", "white", "a5");
		pieceDto = pieceDao.save(pieceDto);

		//when
		pieceDao.update(pieceDto.getId(), newPosition);

		//then
		PieceDto actual = pieceDao.findById(pieceDto.getId()).orElseThrow(NoSuchElementException::new);
		assertThat(actual.getPosition()).isEqualTo(newPosition);
	}

	@DisplayName("게임 아이디에 해당하는 모든 피스를 찾아온다")
	@Test
	void findAllByGameId() throws SQLException {
		//given
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);

		PieceDto pieceDto1 = new PieceDto(gameDto.getId(),"r","white","a1");
		PieceDto pieceDto2 = new PieceDto(gameDto.getId(),"r","white","a2");
		pieceDao.save(pieceDto1);
		pieceDao.save(pieceDto2);

		//when
		List<Piece> pieces = pieceDao.findAllByGameId(gameDto.getId());

		//then
		assertThat(pieces).hasSize(2);
	}
}
