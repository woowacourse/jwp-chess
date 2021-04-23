package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.Position;
import chess.dto.MoveRequest;
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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class MoveDaoTest {

    @Autowired
    private MoveDao moveDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS room cascade");
        jdbcTemplate.execute("create table room("
            + "id int not null auto_increment,"
            + "name varchar(50) not null,"
            + "primary key (id))");

        jdbcTemplate.execute("DROP TABLE IF EXISTS move");
        jdbcTemplate.execute("create table  move("
            + "id int not null auto_increment,"
            + "room_id int,"
            + "start varchar(2) not null,"
            + "end varchar(2) not null,"
            + "primary key (id),"
            + "foreign key (room_id) references room (id) on delete cascade"
            + ")");

        List<Object[]> splitPositions = Arrays.asList("a2 a3", "c7 c6").stream()
            .map(positions -> positions.split(" "))
            .collect(Collectors.toList());
        jdbcTemplate.execute("insert into room (name) values ('test')");
        jdbcTemplate.batchUpdate("INSERT INTO move (start, end, room_id) VALUES (?, ?, 1)", splitPositions);
    }

    @DisplayName("체스말을 이동하면 db에 이동 이력이 삽입된다.")
    @Test
    void addMove() {
        moveDao.addMoveById(Position.of("b7"), Position.of("b5"), 1);
        assertThat(moveDao.getMovesById(1)).hasSize(3);
    }

    @DisplayName("db에서 이동 이력을 불러올 수 있다.")
    @Test
    void getMoves() {
        MoveRequest move = moveDao.getMovesById(1).get(0);
        assertThat(Position.of(move.getFrom())).isEqualTo(Position.of("a2"));
        assertThat(Position.of(move.getTo())).isEqualTo(Position.of("a3"));
    }

    @DisplayName("새로 게임을 시작하면 이동 이력을 모두 삭제한다.")
    @Test
    void deleteAll() {
        moveDao.deleteAllById(1);
        assertThat(moveDao.getMovesById(1)).hasSize(0);
    }
}