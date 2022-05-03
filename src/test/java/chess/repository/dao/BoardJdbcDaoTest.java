package chess.repository.dao;

import static chess.domain.Fixture.E2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.MoveRequest;
import chess.repository.entity.BoardEntity;
import chess.repository.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;
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
class BoardJdbcDaoTest {

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
    @DisplayName("Board 저장 테스트")
    void save() {
        RoomEntity roomEntity = roomDao.save(new RoomEntity("test room", "password", "white", " black"));
        List<BoardEntity> save = boardDao.save(new BoardEntity(roomEntity.getId()));

        assertThat(save.size()).isEqualTo(32);
    }

    @Test
    @DisplayName("Board 조회 테스트")
    void findById() {
        RoomEntity roomEntity = roomDao.save(new RoomEntity("test room", "password", "white", " black"));
        List<BoardEntity> save = boardDao.save(new BoardEntity(roomEntity.getId()));
        List<BoardEntity> foundById = boardDao.findById(roomEntity.getId());

        assertThat(save).isEqualTo(foundById);
    }

    @Test
    @DisplayName("Board 기물 이동 테스트")
    void updateMove() {
        RoomEntity roomEntity = roomDao.save(new RoomEntity("test room", "password", "white", " black"));
        List<BoardEntity> save = boardDao.save(new BoardEntity(roomEntity.getId()));
        boardDao.updateMove(new MoveRequest("pw", "E2", "E4", roomEntity.getId()));
        List<BoardEntity> afterMove = boardDao.findById(roomEntity.getId());

        List<BoardEntity> e2Before = save.stream()
                .filter(entity -> E2.equals(entity.getPosition()))
                .collect(Collectors.toList());

        List<BoardEntity> e2After = afterMove.stream()
                .filter(entity -> E2.equals(entity.getPosition()))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(e2Before).isNotEmpty(),
                () -> assertThat(e2After).isEmpty()
        );
    }

    @Test
    @DisplayName("Board 삭제 테스트")
    void deleteById() {
        RoomEntity roomEntity = roomDao.save(new RoomEntity("test room", "password", "white", " black"));
        List<BoardEntity> save = boardDao.save(new BoardEntity(roomEntity.getId()));
        boardDao.deleteById(roomEntity.getId());
        List<BoardEntity> afterDelete = boardDao.findById(roomEntity.getId());

        assertAll(
                () -> assertThat(save).isNotEmpty(),
                () -> assertThat(afterDelete).isEmpty()
        );
    }
}
