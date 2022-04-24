package chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import chess.configuration.RepositoryConfiguration;
import chess.web.dto.RoomDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RepositoryConfiguration.class)
class RoomServiceTest {

	@Autowired
	private RoomService roomService;

	@Test
	@DisplayName("이름을 받아 체스 게임 방을 생성한다.")
	void create() {
		String name = "summer";
		RoomDto room = roomService.create(name);
		assertThat(room.getName()).isEqualTo(name);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
	@DisplayName("빈 이름이나 16자 초과 이름이 들어오면 예외가 발생한다.")
	void createException(String name) {
		assertThatThrownBy(() -> roomService.create(name))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("없는 id로 방을 조회하면 예외가 발생한다.")
	void validate() {
		assertThatThrownBy(() -> roomService.validateId(1))
			.isInstanceOf(IllegalArgumentException.class);
	}
}