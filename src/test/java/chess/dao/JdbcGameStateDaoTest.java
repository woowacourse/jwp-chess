package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcGameStateDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GameStateDao gameStateDao;
    private RoomDao roomDao;

    private static final int ROOMNUMBER = 1;

    @BeforeEach
    void setUp() {
        gameStateDao = new JdbcGameStateDao(jdbcTemplate);
        roomDao = new JdbcRoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("create table room("
                + "id int auto_increment, name varchar(20) not null,"
                + "password varchar(20) not null)");
        jdbcTemplate.execute("CREATE TABLE game(roomnumber int not null," +
                "state varchar(7)," +
                "turn  varchar(5)," +
                "foreign key (roomnumber) references room(id)," +
                "primary key (roomnumber));");
        roomDao.createRoom("집에 가고 싶다.", "12345678");
    }

    @Test
    @DisplayName("턴 정보를 DB에 저장한다.")
    void saveTurn() {
        //given
        gameStateDao.saveTurn(ROOMNUMBER, "WHITE");
        //when
        final String actual = gameStateDao.getTurn(ROOMNUMBER);
        //then
        assertThat(actual).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("게임 상태를 DB에 저장한다.")
    void saveState() {
        //given
        gameStateDao.saveState(ROOMNUMBER,"playing");
        //when
        final String actual = gameStateDao.getGameState(ROOMNUMBER);
        //then
        assertThat(actual).isEqualTo("playing");
    }
}