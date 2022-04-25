package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:roomDaoTest.sql")
class RoomDaoImplTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl(jdbcTemplate);
    }

    @ParameterizedTest
    @DisplayName("중복되는 체스방 이름이 있는지 반환한다.")
    @CsvSource({"first, true", "second, false"})
    void isDuplicatedName(final String roomName, final boolean expected) {
        //given
        roomDao.saveNewRoom("first", "1234");

        //actual
        final boolean actual = roomDao.isDuplicatedName(roomName);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
