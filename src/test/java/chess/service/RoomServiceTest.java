package chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import chess.configuration.RepositoryConfiguration;
import chess.exception.UserInputException;
import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;

@SpringBootTest
@Import(RepositoryConfiguration.class)
class RoomServiceTest {

	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomRepository roomRepository;

	private final String testName = "summer";
	private final String password = "summer";

	@AfterEach
	void deleteCreated() {
		roomRepository.findAll()
			.forEach(room -> roomRepository.deleteById((int) room.getId()));
	}

	@Test
	@DisplayName("이름을 받아 체스 게임 방을 생성한다.")
	void create() {
		RoomDto room = roomService.create(new RoomDto(testName, password));
		assertThat(room.getName()).isEqualTo(testName);
	}

	@Test
	@DisplayName("이미 있는 이름으로 저장하면 예외가 발생한다.")
	void validateDuplicateName() {
		roomService.create(new RoomDto(testName, password));

		assertThatThrownBy(() -> roomService.create(new RoomDto(testName, password)))
			.isInstanceOf(UserInputException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
	@DisplayName("빈 이름이나 16자 초과 이름이 들어오면 예외가 발생한다.")
	void createException(String name) {
		assertThatThrownBy(() -> roomService.create(new RoomDto(name, password)))
			.isInstanceOf(UserInputException.class);
	}

	@Test
	@DisplayName("없는 id로 방을 조회하면 예외가 발생한다.")
	void validate() {
		assertThatThrownBy(() -> roomService.validateId(0))
			.isInstanceOf(UserInputException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " "})
	@DisplayName("빈 비밀번호를 입력하면 예외가 발생한다.")
	void passwordException(String password) {
		assertThatThrownBy(() -> roomService.create(new RoomDto(testName, password)))
			.isInstanceOf(UserInputException.class);
	}

	@Test
	@DisplayName("id와 비밀번호가 맞지 않으면 삭제하지 못한다.")
	void removeExceptionPassword() {
		RoomDto room = roomService.create(new RoomDto(testName, password));

		assertThatThrownBy(() -> roomService.delete((int)room.getId(), "1234"))
			.isInstanceOf(UserInputException.class);
	}
}