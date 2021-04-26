package chess.dao;

import chess.dto.RoomDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
class SpringChessRoomDaoTest {
    private static final String TEST_ID = "1";
    private static final String TEST_NAME = "testRoom";

    @Autowired
    private SpringChessRoomDao springChessRoomDao;

    @AfterEach
    void deleteLog() {
        springChessRoomDao.delete(TEST_ID);
    }


    @DisplayName("id로 방이 존재하는지 찾는다.")
    @Test
    void findRoomById() {
        springChessRoomDao.add(new RoomDto(TEST_ID, TEST_NAME, "1234"));

        assertThat(springChessRoomDao.findRoomNameById(TEST_ID)).isEqualTo(TEST_NAME);
        assertThatThrownBy(() -> springChessRoomDao.findRoomNameById("2")).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("id로 방이 없는지 확인한다")
    @Test
    void delete() {
        springChessRoomDao.add(new RoomDto(TEST_ID, TEST_NAME, "1234"));

        springChessRoomDao.delete(TEST_ID);
        assertThatThrownBy(() -> springChessRoomDao.findRoomNameById(TEST_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }
}