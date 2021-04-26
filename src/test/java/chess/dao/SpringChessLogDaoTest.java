package chess.dao;

import chess.dto.CommandDto;
import chess.dto.MoveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
class SpringChessLogDaoTest {
    private static final String TEST_ID = "1";

    @Autowired
    private SpringChessLogDao springChessLogDao;

    @AfterEach
    void deleteLog() {
        springChessLogDao.delete(TEST_ID);
    }

    @DisplayName("로그가 정상적으로 등록이 되는지 확인한다.")
    @Test
    void add() {
        springChessLogDao.add(new MoveRequestDto(TEST_ID, "d2", "d4"));
        springChessLogDao.add(new MoveRequestDto(TEST_ID, "a7", "a5"));

        List<CommandDto> commandDtos = springChessLogDao.find(TEST_ID);

        assertThat(commandDtos.get(0).getRoomId()).isEqualTo(TEST_ID);
        assertThat(commandDtos.get(0).getTarget()).isEqualTo("d2");
        assertThat(commandDtos.get(0).getDestination()).isEqualTo("d4");
        assertThat(commandDtos.get(1).getRoomId()).isEqualTo(TEST_ID);
        assertThat(commandDtos.get(1).getTarget()).isEqualTo("a7");
        assertThat(commandDtos.get(1).getDestination()).isEqualTo("a5");
    }

    @DisplayName("로그가 정상적으로 삭제 되는지 확인한다.")
    @Test
    void remove() {
        springChessLogDao.add(new MoveRequestDto(TEST_ID, "d2", "d4"));
        springChessLogDao.add(new MoveRequestDto(TEST_ID, "a7", "a5"));

        springChessLogDao.delete(TEST_ID);

        springChessLogDao.add(new MoveRequestDto(TEST_ID, "c2", "c4"));

        List<CommandDto> commandDtos = springChessLogDao.find(TEST_ID);

        assertThat(commandDtos.get(0).getRoomId()).isEqualTo(TEST_ID);
        assertThat(commandDtos.get(0).getTarget()).isEqualTo("c2");
        assertThat(commandDtos.get(0).getDestination()).isEqualTo("c4");
        assertThat(commandDtos).hasSize(1);
    }
}