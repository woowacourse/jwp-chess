package chess.domain.repository.room;

import chess.domain.manager.Game;
import chess.domain.repository.game.GameRepository;
import chess.domain.repository.game.JdbcGameRepository;
import chess.domain.room.Room;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JdbcRoomRepositoryTest {

    private final RoomRepository roomRepository;
    private final GameRepository gameRepository;

    public JdbcRoomRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.roomRepository = new JdbcRoomRepository(jdbcTemplate);
        this.gameRepository = new JdbcGameRepository(jdbcTemplate);
    }

    @Test
    void save() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);

        //when
        Room room = new Room(gameId, "방 이름", 55L, 56L);
        Long roomId = roomRepository.save(room);

        //then
        assertThat(roomId).isNotNull();
    }

    @Test
    void saveNewRoom() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        Room room = new Room(gameId, "방 이름", 55L);
        Long roomId = roomRepository.saveNewRoom(room);

        //when
        assertThat(roomId).isNotNull();
    }

    @Test
    void updateBlackUser() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        Room room = new Room(gameId, "방 이름", 55L, 56L);
        Long roomId = roomRepository.save(room);

        //when
        Long updateBlackUserId = 556L;
        roomRepository.updateBlackUser(updateBlackUserId, gameId);
        Room updatedRoom = roomRepository.findById(roomId).get();

        //then
        assertThat(updatedRoom.blackUserId()).isEqualTo(updateBlackUserId);
    }

    @Test
    void findById() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        String expectedRoomName = "방 이름";
        Long expectedWhiteUserId = 55L;
        Long expectedBlackUserId = 56L;
        Room room = new Room(gameId, expectedRoomName, expectedWhiteUserId, expectedBlackUserId);
        Long roomId = roomRepository.save(room);

        //when
        Room findRoom = roomRepository.findById(roomId).get();

        //then
        assertThat(expectedRoomName).isEqualTo(findRoom.name());
        assertThat(expectedWhiteUserId).isEqualTo(findRoom.whiteUserId());
        assertThat(expectedBlackUserId).isEqualTo(findRoom.blackUserId());
    }

    @Test
    void findByGameId() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        String expectedRoomName = "방 이름";
        Long expectedWhiteUserId = 55L;
        Long expectedBlackUserId = 56L;
        Room room = new Room(gameId, expectedRoomName, expectedWhiteUserId, expectedBlackUserId);
        roomRepository.save(room);

        //when
        Room findRoom = roomRepository.findByGameId(gameId);

        //then
        assertThat(expectedRoomName).isEqualTo(findRoom.name());
        assertThat(expectedWhiteUserId).isEqualTo(findRoom.whiteUserId());
        assertThat(expectedBlackUserId).isEqualTo(findRoom.blackUserId());
    }

    @Test
    void findByPlayingGame() {
        //given
        saveRoomAndGameTenItems();

        //when
        List<Room> rooms = roomRepository.findByPlayingGame();

        //then
        assertThat(rooms).hasSize(10);
    }

    private void saveRoomAndGameTenItems() {
        for (int i = 0; i < 10; i++) {
            Game game = new Game();
            Long gameId = gameRepository.save(game);
            String expectedRoomName = "방 이름";
            Long expectedWhiteUserId = 55L;
            Long expectedBlackUserId = 56L;
            Room room = new Room(gameId, expectedRoomName, expectedWhiteUserId, expectedBlackUserId);
            roomRepository.save(room);
        }
    }
}