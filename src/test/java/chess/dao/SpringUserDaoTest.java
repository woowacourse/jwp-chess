package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.spring.SpringUserDao;
import chess.domain.board.Team;
import chess.dto.web.UsersInRoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application.properties")
@SpringBootTest
public class SpringUserDaoTest {

    private SpringUserDao springUserDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE `user`");

        jdbcTemplate.execute("TRUNCATE TABLE room");

        jdbcTemplate.execute("INSERT INTO `user` (name) VALUES ('white')");
        jdbcTemplate.execute("INSERT INTO `user` (name) VALUES ('black')");
        jdbcTemplate.execute(
            "INSERT INTO room (id, name, is_opened, white, black) VALUES (1, 'testRoom', true, 'white', 'black')");

        springUserDao = new SpringUserDao(jdbcTemplate);
    }


    @DisplayName("유저 추가 기능")
    @Test
    void insert() {
        springUserDao.insert("testUser");
        jdbcTemplate.query("SELECT count(*) FROM `user` WHERE name = ?",
            (resultSet, rowNum) ->
                assertThat(resultSet.getInt(1)).isEqualTo(1)
            , "testUser");
    }

    @DisplayName("방에 있는 유저들의 전적 반환")
    @Test
    void usersInRoom() {
        UsersInRoomDto usersInRoomDto = springUserDao.usersInRoom("1");
        assertThat(usersInRoomDto.getWhiteName()).isEqualTo("white");
        assertThat(usersInRoomDto.getBlackName()).isEqualTo("black");
        assertThat(usersInRoomDto.getWhiteWin()).isEqualTo("0");
        assertThat(usersInRoomDto.getWhiteLose()).isEqualTo("0");
        assertThat(usersInRoomDto.getBlackWin()).isEqualTo("0");
        assertThat(usersInRoomDto.getBlackLose()).isEqualTo("0");
    }

    @DisplayName("유저 전적을 갱신하는 기능")
    @Test
    void updateStatistics() {
        springUserDao.updateStatistics("1", Team.BLACK);
        UsersInRoomDto usersInRoomDto = springUserDao.usersInRoom("1");
        assertThat(usersInRoomDto.getWhiteName()).isEqualTo("white");
        assertThat(usersInRoomDto.getBlackName()).isEqualTo("black");
        assertThat(usersInRoomDto.getWhiteWin()).isEqualTo("0");
        assertThat(usersInRoomDto.getWhiteLose()).isEqualTo("1");
        assertThat(usersInRoomDto.getBlackWin()).isEqualTo("1");
        assertThat(usersInRoomDto.getBlackLose()).isEqualTo("0");
    }
}
