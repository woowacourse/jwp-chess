package wooteco.chess.database;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.entity.GameRoom;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class GameRoomRepositoryTest {

	@Autowired
	GameRoomRepository gameRoomRepository;

	@DisplayName("이름으로 게임방을 검색하기")
	@Test
	void findByName() {
		String gameRoomName = "test";
		GameRoom savedGameRoom = gameRoomRepository.save(new GameRoom(gameRoomName));

		GameRoom expected = gameRoomRepository.findByName(gameRoomName)
			.orElseThrow(() -> new NoSuchElementException("해당 이름의 방이 존재하지 않습니다."));
		assertThat(savedGameRoom.getName()).isEqualTo(expected.getName());
	}

	@DisplayName("중복된 이름의 게임방 생성시 오류 발생")
	@Test
	void save_duplicatedName_ExceptionThrown() {
		String game = "test";

		gameRoomRepository.save(new GameRoom(game));
		assertThatThrownBy(() -> gameRoomRepository.save(new GameRoom(game)))
			.isInstanceOf(DbActionExecutionException.class);
	}

}