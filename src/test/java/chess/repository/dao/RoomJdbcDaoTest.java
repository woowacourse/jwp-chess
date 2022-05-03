package chess.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.piece.Color;
import chess.repository.entity.RoomEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest(properties = "spring.config.location=classpath:/application-test.yml")
@Sql("/schema-test.sql")
class RoomJdbcDaoTest {

    public static final RoomEntity ROOM_CREATE_FIXTURE = new RoomEntity("test room", "password", "white", "black");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private BoardDao boardDao;
    private RoomDao roomDao;

    @BeforeEach
    void setup() {
        boardDao = new BoardJdbcDao(jdbcTemplate, namedParameterJdbcTemplate);
        roomDao = new RoomJdbcDao(jdbcTemplate);

        boardDao.deleteAll();
        roomDao.deleteAll();
    }

    @Test
    @DisplayName("Room 생성 테스트")
    void save() {
        RoomEntity roomEntity = roomDao.save(ROOM_CREATE_FIXTURE);
        RoomEntity found = roomDao.findById(roomEntity.getId());

        assertThat(found).isNotNull();
    }

    @Test
    @DisplayName("Room 조회 테스트")
    void findById() {
        RoomEntity roomEntity = roomDao.save(ROOM_CREATE_FIXTURE);
        RoomEntity found = roomDao.findById(roomEntity.getId());

        assertThat(found).isNotNull();
    }

    @Test
    @DisplayName("Room 전체 조회 테스트")
    void findAll() {
        RoomEntity roomEntity = roomDao.save(ROOM_CREATE_FIXTURE);
        RoomEntity roomEntity2 = roomDao.save(ROOM_CREATE_FIXTURE);
        List<RoomEntity> found = roomDao.findAll();

        assertThat(found.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Room 삭제 테스트")
    void deleteById() {
        RoomEntity roomEntity = roomDao.save(ROOM_CREATE_FIXTURE);
        RoomEntity found = roomDao.findById(roomEntity.getId());
        long row = roomDao.deleteById(roomEntity.getId());
        RoomEntity afterDelete = roomDao.findById(roomEntity.getId());

        assertAll(
                () -> assertThat(found.isDeleted()).isFalse(),
                () -> assertThat(afterDelete.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("Trun 변경 테스트")
    void changeTurnById() {
        RoomEntity roomEntity = roomDao.save(ROOM_CREATE_FIXTURE);
        RoomEntity found = roomDao.findById(roomEntity.getId());
        roomDao.changeTurnById(roomEntity.getId());
        RoomEntity afterChangeTurn = roomDao.findById(roomEntity.getId());

        assertAll(
                () -> assertThat(found.getTurn()).isEqualTo(Color.WHITE),
                () -> assertThat(afterChangeTurn.getTurn()).isEqualTo(Color.BLACK)
        );
    }

    @Test
    @DisplayName("게임 종료 처리 테스트")
    void finishById() {
        RoomEntity roomEntity = roomDao.save(ROOM_CREATE_FIXTURE);
        RoomEntity found = roomDao.findById(roomEntity.getId());
        roomDao.finishById(roomEntity.getId());
        RoomEntity afterFinish = roomDao.findById(roomEntity.getId());

        assertAll(
                () -> assertThat(found.isFinished()).isFalse(),
                () -> assertThat(afterFinish.isFinished()).isTrue()
        );
    }
}
