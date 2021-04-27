package chess.dao;

import chess.domain.Side;
import chess.exception.room.NotExistRoomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
public class SpringBoardDaoTest {

    private static final String TEST_NAME = "testRoom1";
    private static final String TEST_NAME_2 = "testRoom2";

    @Autowired
    SpringBoardDao springBoardDao;

    @BeforeEach
    void addBoard() {
        springBoardDao.addBoard(TEST_NAME);
        springBoardDao.addBoard(TEST_NAME_2);
    }

    @AfterEach
    void deleteBoard() {
        springBoardDao.deleteRoom(TEST_NAME);
        springBoardDao.deleteRoom(TEST_NAME_2);
    }

    @Test
    @DisplayName("DB에 board 추가 및 조회 테스트")
    void testAdd() {
        List<String> rooms = springBoardDao.findRooms();

        assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("DB에 turn 조회 테스트")
    void testFindTurn() {
        Side side = springBoardDao.findTurn(TEST_NAME).orElseThrow(NotExistRoomException::new);


        assertThat(side.name()).isEqualTo("WHITE");
    }
}
