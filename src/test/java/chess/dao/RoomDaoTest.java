package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import java.sql.PreparedStatement;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RoomDaoTest {
    private final RoomDao roomDao;
    private final JdbcTemplate jdbcTemplate;

    private RoomDaoTest(final RoomDao roomDao, final JdbcTemplate jdbcTemplate) {
        this.roomDao = roomDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        jdbcTemplate.execute("TRUNCATE TABLE room;ALTER TABLE room ALTER COLUMN room_id RESTART WITH 1;");

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("insert into game (is_end) values (?)");
            preparedStatement.setBoolean(1, chessGame.isEnd());
            return preparedStatement;
        });
        String sql = "insert into room (name, pw, game_id) values (?, ?, ?)";

        Room room = new Room(1L, "멍토", "1234", 1L);
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, room.getName());
            preparedStatement.setString(2, room.getPw());
            preparedStatement.setLong(3, room.getGameId());
            return preparedStatement;
        });
    }

    @Test
    @DisplayName("방 생성 테스트")
    void create() {
        Room room = new Room(1L, "망토", "12345", 1L);
        assertThatCode(() -> roomDao.create(room)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("데이터 로드")
    void load() {
        Room room = roomDao.load(1);
        assertThat(room.getGameId()).isEqualTo(1);
        assertThat(room.getId()).isEqualTo(1);
        assertThat(room.getName()).isEqualTo("멍토");
        assertThat(room.getPw()).isEqualTo("1234");
    }

    @Test
    @DisplayName("모든 방 정보 가져오기")
    void loadAll() {
        List<Room> rooms = roomDao.loadAll();
        System.out.println(rooms);
        assertThat(rooms).hasSize(1);
    }
}