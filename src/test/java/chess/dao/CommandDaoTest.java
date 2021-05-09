package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommandDaoTest {
    @Autowired
    private GameDao gameDao;
    @Autowired
    private CommandDao commandDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        gameDao.insert("test");
    }

    @Test
    @DisplayName("게임 id에 해당하는 커맨드 긁어오기")
    void findAllCommand() {
        List<Object[]> splitUpNames = Stream.of("1 a7 a5", "1 b2 b3", "1 a5 a4", "1 b3 a4")
                                            .map(position -> position.split(" "))
                                            .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO command(game_id, move_from, move_to) VALUES (?, ?, ?)", splitUpNames);
        assertThat(commandDAO.findAllCommandOf(1L)).hasSize(4);
    }

    @Test
    @DisplayName("정상 커맨드 추가")
    void insert() {
        commandDAO.insert(1L, "c2", "c4");
        assertThat(commandDAO.findAllCommandOf(1L)).hasSize(1);
    }

    @Test
    @DisplayName("생성되지 않는 방에 커맨드 추가")
    void insertNonExistGame() {
        assertThatThrownBy(() -> commandDAO.insert(10L, "c2", "c4"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}