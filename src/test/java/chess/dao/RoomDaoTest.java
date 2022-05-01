package chess.dao;

import static org.assertj.core.api.Assertions.*;

import chess.dto.RoomDto;
import chess.entity.GameEntity;
import chess.entity.RoomEntity;
import java.util.List;
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
        gameDao = new GameDbDao(jdbcTemplate);
        roomDao = new RoomDbDao(jdbcTemplate);

        jdbcTemplate.execute("create table game("
                + "game_id int primary key auto_increment,"
                + " current_turn varchar(10) default'WHITE')");
        jdbcTemplate.update("insert into game(current_turn) values (?)", "WHITE");

        jdbcTemplate.execute("create table room("
                + "room_id int primary key auto_increment,"
                + "game_id int not null,"
                + "room_name varchar(10) not null,"
                + "room_password varchar(10) not null,"
                + "status varchar(4) default'STOP',"
                + "foreign key (game_id) references game(game_id))");

        roomDao.insert(RoomEntity.of(1,"josh", "0000", "PLAY"));
    }

    @AfterEach
    void clean() {
        roomDao.delete(RoomEntity.of(1));
        gameDao.deleteById(GameEntity.of(1));
        jdbcTemplate.execute("drop table room if exists");
        jdbcTemplate.execute("drop table game if exists");
    }

    @Test
    void findByIdTest() {
        RoomEntity roomEntity = roomDao.findById(RoomEntity.of(1));
        assertThat(roomEntity.getRoomPassword().equals("0000")).isTrue();
    }

    @Test
    void findAllTest() {
        gameDao.insert(GameEntity.of(2));
        roomDao.insert(RoomEntity.of(2, "pobi", "1234", "PLAY"));
        List<RoomEntity> roomEntities = roomDao.findAll();
        assertThat(roomEntities.size() == 2).isTrue();
    }

    @Test
    void updateByIdTest() {
        RoomEntity roomEntity = roomDao.findById(RoomEntity.of(1));
        roomDao.updateById(RoomEntity.of(roomEntity.getGameId(),
                roomEntity.getRoomName(),
                roomEntity.getRoomPassword(),
                "STOP"));
        assertThat(roomDao.findById(RoomEntity.of(1)).getStatus()).isEqualTo("STOP");

    }
}
