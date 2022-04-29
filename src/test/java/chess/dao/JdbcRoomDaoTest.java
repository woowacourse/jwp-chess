package chess.dao;

import static chess.domain.GameStatus.PLAYING;
import static chess.domain.GameStatus.READY;
import static chess.domain.chesspiece.Color.BLACK;
import static chess.domain.chesspiece.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.entity.RoomEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcRoomDaoTest {

    private static final RoomEntity ROOM_ENTITY = new RoomEntity(0, "매트의 체스", "123123",
            READY.getValue(), WHITE.getValue());

    private final JdbcRoomDao roomDao;

    @Autowired
    public JdbcRoomDaoTest(JdbcTemplate jdbcTemplate) {
        this.roomDao = new JdbcRoomDao(jdbcTemplate);
    }

    @DisplayName("RoomSaveDto를 전달 받아 room entity를 저장한다.")
    @Test
    void room_저장한다() {
        final int id = roomDao.save(ROOM_ENTITY);

        assertThat(roomDao.findById(id).isPresent()).isTrue();
    }

    @DisplayName("room id를 활용하여 entity를 조회한다.")
    @Test
    void room_조회한다() {
        final int id = roomDao.save(ROOM_ENTITY);

        final RoomEntity room = roomDao.findById(id).get();

        assertAll(
                () -> assertThat(id).isEqualTo(room.getId()),
                () -> assertThat("매트의 체스").isEqualTo(room.getName()),
                () -> assertThat("123123").isEqualTo(room.getPassword()),
                () -> assertThat(READY.getValue()).isEqualTo(room.getGameStatus()),
                () -> assertThat(WHITE.getValue()).isEqualTo(room.getCurrentTurn())
        );
    }

    @DisplayName("존재하지 않는 id의 room을 조회한다.")
    @Test
    void room_조회_실패한다() {
        final boolean result = roomDao.findById(26).isEmpty();

        assertThat(result).isTrue();
    }

    @DisplayName("room 존재 여부를 확인한다.")
    @Test
    void room_존재_여부를_확인한다() {
        int id = roomDao.save(ROOM_ENTITY);

        boolean result = roomDao.existsById(id);

        assertThat(result).isTrue();
    }

    @DisplayName("모든 room을 조회한다.")
    @Test
    void room_모두_조회한다() {
        roomDao.save(ROOM_ENTITY);
        roomDao.save(ROOM_ENTITY);
        roomDao.save(ROOM_ENTITY);

        List<RoomEntity> rooms = roomDao.findAll();

        assertThat(rooms.size()).isEqualTo(3);
    }

    @DisplayName("room을 수정 한다.")
    @Test
    void room_업데이트한다() {
        int id = roomDao.save(ROOM_ENTITY);

        roomDao.update(new RoomEntity(id, ROOM_ENTITY.getName(), ROOM_ENTITY.getPassword(), PLAYING.getValue(),
                BLACK.getValue()));

        final RoomEntity room = roomDao.findById(id).get();

        assertAll(
                () -> assertThat(id).isEqualTo(room.getId()),
                () -> assertThat(PLAYING.getValue()).isEqualTo(room.getGameStatus()),
                () -> assertThat(BLACK.getValue()).isEqualTo(room.getCurrentTurn())
        );
    }

    @DisplayName("id를 기반으로 room을 삭제한다.")
    @Test
    void room_삭제한다() {
        final int id = roomDao.save(ROOM_ENTITY);

        roomDao.deleteByIdAndPassword(id, "123123");

        assertThat(roomDao.findById(id).isEmpty()).isTrue();
    }
}
