package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootTest
class RoomServiceTest {

    private RoomDao roomDao;

    @Autowired
    private RoomService roomService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute(""
                + "CREATE TABLE room"
                + "("
                + "    room_id      INT         PRIMARY KEY AUTO_INCREMENT,"
                + "    name         VARCHAR(10) NOT NULL UNIQUE,"
                + "    game_status  VARCHAR(10) NOT NULL,"
                + "    current_turn VARCHAR(10) NOT NULL,"
                + "    password     VARCHAR(255) NOT NULL"
                + ")");
    }

    @Test
    @DisplayName("방을 생성한다.")
    void createRoom() {
        // given
        final String roomName = "test";
        final String password = "1234";
        final RoomCreationRequestDto creationRequestDto = new RoomCreationRequestDto(roomName, password);

        // when
        final int roomId = roomService.createRoom(creationRequestDto);

        // then
        assertThat(roomId).isEqualTo(1);
    }

    @Test
    @DisplayName("생성하려는 방 이름이 중복이면 예외가 터진다.")
    void createRoom_exception() {
        // given
        final String roomName = "test";
        final String password = "1234";
        roomDao.save(roomName, GameStatus.READY, Color.WHITE, password);

        // when
        final RoomCreationRequestDto creationRequestDto = new RoomCreationRequestDto(roomName, password);

        // then
        assertThatThrownBy(() -> roomService.createRoom(creationRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름이 같은 방이 이미 존재합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 방을 삭제하면 예외가 터진다.")
    void deleteRoom_not_exist() {
        // given
        final int roomId = 1;
        final String password = "1234";
        final RoomDeletionRequestDto dto = new RoomDeletionRequestDto(roomId, password);

        // then
        assertThatThrownBy(() -> roomService.deleteRoom(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 방 입니다.");
    }

    @ParameterizedTest
    @DisplayName("요청의 비밀번호 값이 존재하지 않으면 예외가 터진다.")
    @ValueSource(strings = {"", "   "})
    void deleteRoom_blank_password(final String password) {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");
        final RoomDeletionRequestDto dto = new RoomDeletionRequestDto(roomId, password);

        // then
        assertThatThrownBy(() -> roomService.deleteRoom(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청에 비밀번호가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("삭제하려는 방의 비밀번호가 일치하지 않으면 예외가 터진다.")
    void deleteRoom_password_not_match() {
        // given
        final String password = BCrypt.hashpw("1234", BCrypt.gensalt());
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, password);
        final RoomDeletionRequestDto dto = new RoomDeletionRequestDto(roomId, "4321");

        // then
        assertThatThrownBy(() -> roomService.deleteRoom(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("게임이 진행 중인 방을 삭제하면 예외가 발생한다.")
    void deleteRoom_exception_not_end() {
        // given
        final String password = "1q2w3e4r";
        final String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        final int roomId = roomDao.save("hi", GameStatus.PLAYING, Color.WHITE, hashPassword);

        // then
        final RoomDeletionRequestDto deletionRequestDto = new RoomDeletionRequestDto(roomId, password);

        assertThatThrownBy(() -> roomService.deleteRoom(deletionRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게임이 진행 중입니다.");
    }
}