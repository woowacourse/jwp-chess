package chess.dao.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.board.Board;
import chess.domain.game.Room;
import chess.domain.gamestate.running.Ready;
import chess.domain.team.Team;
import chess.dao.piece.JdbcPieceDao;
import chess.utils.BoardUtil;
import java.util.List;
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
    void findAll() {
        // given
        Room room1 = new Room(1, "테스트1", new Ready(Board.EMPTY_BOARD), Team.WHITE);
        Room room2 = new Room(2, "테스트2", new Ready(Board.EMPTY_BOARD), Team.WHITE);
        Room room3 = new Room(3, "테스트3", new Ready(Board.EMPTY_BOARD), Team.WHITE);
        long room1Id = roomDao.insert(room1);
        long room2Id = roomDao.insert(room2);
        long room3Id = roomDao.insert(room3);

        // when
        List<Room> rooms = roomDao.findAll();

        // then
        assertThat(rooms.size()).isEqualTo(3);
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
