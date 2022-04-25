package chess.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.RoomCreationRequestDto;
import chess.dto.RoomDeletionRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
                + "    password     VARCHAR(10) NOT NULL"
                + ")");
    }

    @Test
    @DisplayName("게임이 진행 중인 방을 삭제하면 예외가 발생한다.")
    void deleteRoom_exception_not_end() {
        // given
        final String password = "1q2w3e4r";
        final int roomId = roomDao.save("hi", GameStatus.PLAYING, Color.WHITE, password);

        // then
        final RoomDeletionRequestDto deletionRequestDto = new RoomDeletionRequestDto();
        deletionRequestDto.setRoomId(roomId);
        deletionRequestDto.setPassword(password);

        assertThatThrownBy(() -> roomService.deleteRoom(deletionRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게임이 진행 중입니다.");
    }
}