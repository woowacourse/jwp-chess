package chess.repository.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.piece.JdbcPieceDao;
import chess.dao.room.JdbcRoomDao;
import chess.domain.game.Room;
import chess.domain.gamestate.running.Ready;
import chess.domain.team.Team;
import chess.utils.BoardUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JdbcRoomDaoTest {

    @Autowired
    JdbcRoomDao roomDao;

    @Autowired
    JdbcPieceDao pieceDao;

    @BeforeEach
    void setUp() {
        pieceDao.deleteAll();
        roomDao.deleteAll();
    }

    @Test
    void insert() {
        // given
        Room room = new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);

        // when
        long roomId = roomDao.insert(room);
        Room foundRoom = roomDao.findRoomByName(room.getName());

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
        long roomId = roomDao
            .insert(new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));
        Room foundRoom = roomDao.findRoomById(roomId);

        // when
        foundRoom.play("start");
        roomDao.update(foundRoom);

        // then
        Room resultRoom = roomDao.findRoomById(roomId);
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
        Room room = new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        long roomId = roomDao.insert(room);

        // when
        Room foundRoom = roomDao.findRoomById(roomId);

        // then
        assertAll(
            () -> assertThat(foundRoom.getId()).isEqualTo(roomId),
            () -> assertThat(foundRoom.getName()).isEqualTo(room.getName()),
            () -> assertThat(foundRoom.getState().getValue()).isEqualTo(room.getState().getValue()),
            () -> assertThat(foundRoom.getCurrentTeam()).isEqualTo(room.getCurrentTeam())
        );
    }

    @Test
    void findRoomByRoomName() {
        // given
        Room room = new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        long roomId = roomDao.insert(room);

        // when
        Room foundRoom = roomDao.findRoomByName(room.getName());

        // then
        assertAll(
                () -> assertThat(foundRoom.getId()).isEqualTo(roomId),
                () -> assertThat(foundRoom.getName()).isEqualTo(room.getName()),
                () -> assertThat(foundRoom.getState().getValue()).isEqualTo(room.getState().getValue()),
                () -> assertThat(foundRoom.getCurrentTeam()).isEqualTo(room.getCurrentTeam())
        );
    }

    @Test
    void isExistRoomName() {
        // given, when
        long roomId = roomDao
            .insert(new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));

        // then
        assertThat(roomDao.isExistName("테스트")).isFalse();
    }
}
