package chess.Dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chess.Dto.MoveRequest;
import chess.domain.position.Position;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class MoveDaoTest {

    @Autowired
    private MoveDao moveDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS move");
        jdbcTemplate.execute("create table  move("
            + "id int not null auto_increment,"
            + "start varchar(2) not null,"
            + "end varchar(2) not null,"
            + "primary key (id)"
            + ");");

        List<Object[]> splitPositions = Arrays.asList("a2 a3", "c7 c6").stream()
            .map(positions -> positions.split(" "))
            .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO move (start, end) VALUES (?, ?)", splitPositions);
    }

    @DisplayName("체스말을 이동하면 db에 이동 이력이 삽입된다.")
    @Test
    void addMove() {
        moveDao.addMove(Position.of("b7"), Position.of("b5"));
        assertThat(moveDao.getMoves()).hasSize(3);
    }

    @DisplayName("db에서 이동 이력을 불러올 수 있다.")
    @Test
    void getMoves() {
        MoveRequest move = moveDao.getMoves().get(0);
        assertThat(move.getFrom()).isEqualTo(Position.of("a2"));
        assertThat(move.getTo()).isEqualTo(Position.of("a3"));
    }

    @DisplayName("새로 게임을 시작하면 이동 이력을 모두 삭제한다.")
    @Test
    void deleteAll() {
        moveDao.deleteAll();
        assertThat(moveDao.getMoves()).hasSize(0);
    }
}