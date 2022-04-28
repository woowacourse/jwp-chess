package chess.dao;

import static org.assertj.core.api.Assertions.*;

import chess.dto.RoomDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class RoomDaoTest {
    private GameDao gameDao;
    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        roomDao = new RoomDao(jdbcTemplate);

        jdbcTemplate.execute("create table game("
                + "game_id int primary key not null,"
                + " current_turn varchar(10) default'WHITE')");
        jdbcTemplate.update("insert into game(game_id, current_turn) values (?,?)",
                0, "WHITE");

        jdbcTemplate.execute("create table room("
                + "room_id int primary key auto_increment,"
                + "game_id int not null,"
                + "room_name varchar(10) not null,"
                + "room_password varchar(10) not null,"
                + "foreign key (game_id) references game(game_id))");

        roomDao.saveRoom(0, new RoomDto("josh", "0000"));
    }

    @AfterEach
    void clean() {
        roomDao.deleteRoom(0);
        jdbcTemplate.execute("drop table room if exists");
        jdbcTemplate.execute("drop table game if exists");
    }
    
    @Test
    void findByIdTest() {
        int gameId = 0;
        RoomDto roomDto = roomDao.findById(gameId);
        assertThat(roomDto.getName().equals("josh") && roomDto.getPassword().equals("0000"))
                .isTrue();
    }

    @Test
    void findPasswordById() {
        int gameId = 0;
        String password = roomDao.findPasswordById(gameId);
        assertThat(password.equals("0000")).isTrue();
    }
}
