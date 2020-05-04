package wooteco.chess.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.Ready;

@AutoConfigureTestDatabase
@SpringBootTest
public class ChessGameRepositoryTest {
	@Autowired
	ChessGameRepository chessGameRepository;

	@BeforeEach
	void cleanup() {
		chessGameRepository.deleteAll();
	}

	ChessGameEntity createGame() {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		return chessGameRepository.save(new ChessGameEntity(chessGame));
	}

	@Test
	@DisplayName("게임 불러오기")
	void find() {
		ChessGameEntity chessGameEntity1 = createGame();
		ChessGameEntity chessGameEntity2 = chessGameRepository.findById(chessGameEntity1.getId()).get();
		assertThat(chessGameEntity1.getId()).isEqualTo(chessGameEntity2.getId());
		assertThat(chessGameEntity1.getBoard()).isEqualTo(chessGameEntity2.getBoard());
		assertThat(chessGameEntity1.getTurn()).isEqualTo(chessGameEntity2.getTurn());
		assertThat(chessGameEntity1.getState()).isEqualTo(chessGameEntity2.getState());
	}

	@Test
	@DisplayName("게임 저장")
	void save() {
		assertThat(createGame().getId()).isNotNull();
	}

	@Test
	@DisplayName("게임 삭제")
	void delete() {
		ChessGameEntity persistChessGameEntity = createGame();
		chessGameRepository.delete(persistChessGameEntity);
		assertThat(chessGameRepository.findById(persistChessGameEntity.getId()).isPresent()).isFalse();
	}

	@Test
	@DisplayName("게임 번호 목록")
	void findRoomIds() {
		Long entity1 = createGame().getId();
		Long entity2 = createGame().getId();
		Long entity3 = createGame().getId();
		assertThat(chessGameRepository.findRoomIds().containsAll(Arrays.asList(entity1, entity2, entity3))).isTrue();
	}
}
