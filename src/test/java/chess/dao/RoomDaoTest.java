package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.entity.RoomEntity;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"schema.sql", "test-data.sql"})
public class RoomDaoTest {

    private static final String NAME = "test game";
    private static final String PASSWORD = "1234";

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("룸을 생성한다.")
    void save() {
        final RoomEntity roomEntity = new RoomEntity("다 드루와", "1234567", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final RoomEntity savedRoom = roomDao.save(roomEntity);
        System.out.println("생성된 룸의 아이디: " + savedRoom.getId());

        assertThat(savedRoom.getName()).isEqualTo("다 드루와");
    }

    @Test
    @DisplayName("모든 룸을 조회한다.")
    void findAll() {
        assertThat(roomDao.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("룸을 조회한다.")
    void findById() {
        final RoomEntity foundRoom = roomDao.findById(1L);

        assertThat(foundRoom.getName()).isEqualTo(NAME);
        assertThat(foundRoom.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("룸의 상태(status)를 변경한다.")
    void updateStatusById() {
        roomDao.updateStatusById(1L, GameStatus.END.getValue());

        assertThat(roomDao.findById(1L).getStatus()).isEqualTo(GameStatus.END.getValue());
    }

    @Test
    @DisplayName("룸의 턴(turn)을 변경한다.")
    void updateTurnById() {
        roomDao.updateTurnById(1L, Color.BLACK.getValue());

        assertThat(roomDao.findById(1L).getTurn()).isEqualTo(Color.BLACK.getValue());
    }

    @Test
    @DisplayName("룸을 삭제한다.")
    void deleteById() {
        roomDao.deleteById(1L);

        assertThat(roomDao.findAll()).hasSize(0);
    }
}
