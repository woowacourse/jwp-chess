package chess.dao.jdbctemplate;

import chess.dto.RoomResponseDto;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class JdbcTemplateRoomDaoTest {

    private JdbcTemplateRoomDao roomDao;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcTemplateRoomDao(dataSource);
    }

    @DisplayName("방을 만든다.")
    @Test
    void create() {
        // given
        String name = "수달방";
        String pw = "1111";
        // when

        roomDao.create(name, pw);
        // then
        List<RoomResponseDto> rooms = roomDao.getRooms();

        Assertions.assertEquals(rooms.get(0).getName(), name);
        Assertions.assertEquals(rooms.get(0).getPw(), pw);
    }
}
