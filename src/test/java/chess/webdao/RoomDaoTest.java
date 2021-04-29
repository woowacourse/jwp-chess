package chess.webdao;

import chess.webdto.dao.TurnDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@ActiveProfiles("test")
class RoomDaoTest {
    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.roomDao = new RoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS board;\n" +
                "DROP TABLE IF EXISTS room;\n" +
                "CREATE TABLE room(" +
                "    room_id    BIGINT      NOT NULL AUTO_INCREMENT,\n" +
                "    turn       VARCHAR(16) NOT NULL,\n" +
                "    is_playing BOOLEAN     NOT NULL,\n" +
                "    name       VARCHAR(16) NOT NULL,\n" +
                "    password   VARCHAR(16),\n" +
                "    create_at  DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                "    PRIMARY KEY (room_id)\n" +
                ");\n" +
                "CREATE TABLE board(" +
                "    board_id       BIGINT      NOT NULL AUTO_INCREMENT,\n" +
                "    team           VARCHAR(16) NOT NULL,\n" +
                "    position       VARCHAR(16) NOT NULL,\n" +
                "    piece          VARCHAR(16) NOT NULL,\n" +
                "    is_first_moved BOOLEAN     NOT NULL,\n" +
                "    room_id        BIGINT      NOT NULL,\n" +
                "    PRIMARY KEY (board_id),\n" +
                "    FOREIGN KEY (room_id) REFERENCES room (room_id)\n" +
                ");");
    }

    @Test
    @DisplayName("방만들기 - 만들어진 roomId 확인")
    void createRoom() {
        roomDao.createRoom("white", true, "sample");
        long secondRoomId = roomDao.createRoom("white", true, "sample");
        roomDao.createRoom("white", true, "addoneMore");

        assertThat(secondRoomId).isEqualTo(2L);
    }

    @Test
    @DisplayName("조회 - 방번호로 현재 턴")
    void selectTurnByRoomId() {
        long roomId = roomDao.createRoom("black", true, "sample");

        TurnDto turnDto = roomDao.selectTurnByRoomId(roomId);

        assertThat(turnDto.getTurn()).isEqualTo("black");
    }

    @Test
    @DisplayName("업데이트 - room 턴정보 변경")
    void changeTurnByRoomId() {
        long roomId = roomDao.createRoom("white", true, "sample");

        TurnDto before = roomDao.selectTurnByRoomId(roomId);
        assertThat(before.getTurn()).isEqualTo("white");

        roomDao.changeTurnByRoomId("black", true, roomId);

        TurnDto after = roomDao.selectTurnByRoomId(roomId);
        assertThat(after.getTurn()).isEqualTo("black");
    }

    @Test
    @DisplayName("삭제 - roomId로 삭제")
    void deleteRoomByRoomId(){
        long roomId1 = roomDao.createRoom("white", true, "sample");
        long roomId2 = roomDao.createRoom("white", true, "sample");

        roomDao.deleteRoomByRoomId(roomId2);

        assertThat(roomDao.selectAllRooms()).hasSize(1);
    }

    @Test
    @DisplayName("모든 방 조회")
    void selectAllRooms() {
        roomDao.createRoom("white", true, "sample");
        roomDao.createRoom("white", true, "sample");

        assertThat(roomDao.selectAllRooms()).hasSize(2);
    }
}