package chess.repository.room;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JdbcRoomRepositoryTest {

    @Autowired
    JdbcRoomRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void insert() {
        // given
        Room room = new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);

        // when
        long roomId = repository.insert(room);
        Room foundRoom = repository.findByName(room.getName());

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
        long roomId = repository.insert(new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));
        Room foundRoom = repository.findById(roomId);

        // when
        foundRoom.play("start");
        repository.update(foundRoom);

        // then
        Room resultRoom = repository.findById(roomId);
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
        long roomId = repository.insert(room);

        // when
        Room foundRoom = repository.findById(roomId);

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
        long roomId = repository.insert(room);

        // when
        Room foundRoom = repository.findByName(room.getName());

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
        long roomId = repository.insert(new Room(0, "테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));

        // then
        assertThat(repository.isExistRoomName("테스트")).isFalse();
    }
}
