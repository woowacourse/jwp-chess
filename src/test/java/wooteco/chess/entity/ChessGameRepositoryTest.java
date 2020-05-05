package wooteco.chess.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.service.exception.InvalidGameException;

@Import(RandomUUIDProvider.class)
@DataJdbcTest
class ChessGameRepositoryTest {
	@Autowired
	private ChessGameRepository chessGameRepository;

	private ChessGameEntity generateNewChessGameEntity() {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		return new ChessGameEntity(chessGame);
	}

	@DisplayName("생성된 모든 체스게임을 검색")
	@Test
	void findRoomIds() {
		List<ChessGameEntity> chessGameEntities = new ArrayList<>();
		chessGameEntities.add(generateNewChessGameEntity());
		chessGameEntities.add(generateNewChessGameEntity());

		chessGameRepository.saveAll(chessGameEntities);

		List<String> roomIds = chessGameRepository.findRoomIds();
		assertThat(roomIds.size()).isEqualTo(2);
	}

	@DisplayName("새로운 체스 게임을 생성하여 저장")
	@Test
	void save() {
		ChessGameEntity chessGameEntity = chessGameRepository.save(generateNewChessGameEntity());
		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(chessGameEntity.getId())
				.orElseThrow(InvalidGameException::new);

		assertThat(chessGameEntity.getId()).isEqualTo(persistChessGameEntity.getId());
	}

	@DisplayName("체스 말을 움직인 후 업데이트")
	@Test
	void update() {
		ChessGameEntity chessGameEntity = chessGameRepository.save(generateNewChessGameEntity());

		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		chessGame.move(Position.from("B2"), Position.from("B3"));
		chessGameEntity.update(chessGame);
		chessGameRepository.save(chessGameEntity);

		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(chessGameEntity.getId())
				.orElseThrow(InvalidGameException::new);
		assertThat(persistChessGameEntity.getBoard()).isEqualTo(chessGameEntity.getBoard());
	}

	@DisplayName("체스 게임이 진행되는 방을 제거")
	@Test
	void delete() {
		ChessGameEntity chessGameEntity = chessGameRepository.save(generateNewChessGameEntity());

		chessGameRepository.delete(chessGameEntity);

		assertThat(chessGameRepository.findById(chessGameEntity.getId())).isEmpty();
	}
}