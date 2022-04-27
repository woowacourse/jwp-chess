package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class PlayerDaoImplTest {
    private RoomDao roomDao;

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        roomDao = new RoomDaoImpl(jdbcTemplate);
//
//        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
//        jdbcTemplate.execute("create table room\n"
//                + "(\n"
//                + "    id int(10) NOT NULL AUTO_INCREMENT,\n"
//                + "    title varchar(255),\n"
//                + "    password varchar(255) NOT NULL,\n"
//                + "    color varchar(5) NOT NULL DEFAULT 'WHITE',\n"
//                + "    primary key (id)\n"
//                + ");\n");
//
//        roomDao.createRoom(new CreateRoomRequestDto("testTitle", "testPassword"));
//    }
//
//
//    @Test
//    void saveTurn() {
//        Player player = roomDao.getPlayer();
//        assertThat(player.name()).isEqualTo(Color.WHITE.name());
//    }
//
//    @Test
//    void deleteAll() {
//        roomDao.deleteAll();
//        assertThatThrownBy(() -> roomDao.getPlayer())
//                .isInstanceOf(EmptyResultDataAccessException.class);
//    }
//
//    @Test
//    void getPlayer() {
//        Player player = roomDao.getPlayer();
//        assertThat(player).isNotNull();
//    }
}
