package chess.dao.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.game.Room;
import chess.domain.gamestate.running.Ready;
import chess.domain.team.Team;
import chess.utils.BoardUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JdbcRoomDaoTest {

    @Autowired
    JdbcRoomDao jdbcRoomDao;

    @BeforeEach
    void setUp() {
//        repository.deleteAll();
    }

    @Test
    void insert() {
        // given
        Room room = new Room(0, "테스트1", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);

        // when
        long roomId = jdbcRoomDao.insert(room);
        Room foundRoom = jdbcRoomDao.findByName(room.getName());

        // then
        assertAll(
            () -> assertThat(foundRoom.getId()).isEqualTo(roomId),
            () -> assertThat(foundRoom.getName()).isEqualTo(room.getName()),
            () -> assertThat(foundRoom.getState().getValue()).isEqualTo(room.getState().getValue()),
            () -> assertThat(foundRoom.getCurrentTeam()).isEqualTo(room.getCurrentTeam())
        );
    }

    @Test
    void update() {
        // given
        long roomId = jdbcRoomDao
                .insert(new Room(0, "테스트2", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));
        Room foundRoom = jdbcRoomDao.findById(roomId);

        // when
        foundRoom.play("start");
        jdbcRoomDao.update(foundRoom);

        // then
        Room resultRoom = jdbcRoomDao.findById(roomId);
        assertAll(
            () -> assertThat(resultRoom.getId()).isEqualTo(roomId),
            () -> assertThat(resultRoom.getName()).isEqualTo(foundRoom.getName()),
            () -> assertThat(resultRoom.getState().getValue()).isEqualTo("start"),
            () -> assertThat(resultRoom.getCurrentTeam()).isEqualTo(foundRoom.getCurrentTeam())
        );
    }

    @Test
    void findRoomById() {
        // given
        Room room = new Room(0, "테스트3", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        long roomId = jdbcRoomDao.insert(room);

        // when
        Room foundRoom = jdbcRoomDao.findById(roomId);

        // then
        assertAll(
            () -> assertThat(foundRoom.getId()).isEqualTo(roomId),
            () -> assertThat(foundRoom.getName()).isEqualTo(room.getName()),
            () -> assertThat(foundRoom.getState().getValue()).isEqualTo(room.getState().getValue()),
            () -> assertThat(foundRoom.getCurrentTeam()).isEqualTo(room.getCurrentTeam())
        );
    }

    @Test
    void findRoomByName() {
        // given
        Room room = new Room(0, "테스트4", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        long roomId = jdbcRoomDao.insert(room);

        // when
        Room foundRoom = jdbcRoomDao.findByName(room.getName());

        // then
        assertAll(
                () -> assertThat(foundRoom.getId()).isEqualTo(roomId),
                () -> assertThat(foundRoom.getName()).isEqualTo(room.getName()),
                () -> assertThat(foundRoom.getState().getValue()).isEqualTo(room.getState().getValue()),
                () -> assertThat(foundRoom.getCurrentTeam()).isEqualTo(room.getCurrentTeam())
        );
    }

    @Test
    void roomExists() {
        // given, when
        long roomId = jdbcRoomDao
                .insert(new Room(0, "테스트5", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));

        // then
        assertThat(jdbcRoomDao.roomExists("테스트5")).isTrue();
    }
}
