package chess.domain.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
	@Test
	@DisplayName("문자열이 20 초과이면 오류")
	void exception() {
		assertThatThrownBy(() -> {
			Room room = new Room("안녕하세요. 여기는 초보 게임방입니다.");
		}).isInstanceOf(IllegalArgumentException.class);
	}
}