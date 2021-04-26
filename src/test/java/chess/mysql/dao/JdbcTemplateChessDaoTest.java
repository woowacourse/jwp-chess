package chess.mysql.dao;

import chess.mysql.dao.dto.ChessGameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("jdbcTemplateChessDao 테스트")
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class JdbcTemplateChessDaoTest {
    private final ChessGameDto sample = new ChessGameDto(1L, "WHITE", true, "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr");

    private JdbcTemplate jdbcTemplate;
    private JdbcTemplateChessDao jdbcTemplateChessDao;

    @Autowired
    public JdbcTemplateChessDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateChessDao = new JdbcTemplateChessDao(jdbcTemplate);
    }

    @DisplayName("저장 및 아이디로 chessGame을 찾아오는 기능 테스트")
    @Test
    void saveAndFindByIdTest() {
        //given
        //when
        ChessGameDto chessGameDto = jdbcTemplateChessDao.save(sample);

        //then
        assertThat(chessGameDto.getNextTurn()).isEqualTo("WHITE");
    }

    @DisplayName("수정 기능 테스트")
    @Test
    void update() {
        //given
        String changedTurn = "BLACK";
        String expectedPieces = ".................RNBQKBNRPPPPPPPP...............pppppppprnbqkbnr";

        //when
        ChessGameDto chessGameDto = jdbcTemplateChessDao.save(sample);
        jdbcTemplateChessDao.update(new ChessGameDto(chessGameDto.getId(), changedTurn, true, expectedPieces));
        ChessGameDto result = jdbcTemplateChessDao.findById(chessGameDto.getId()).get();

        //then
        assertThat(result.getPieces()).isEqualTo(expectedPieces);
        assertThat(result.getNextTurn()).isEqualTo(changedTurn);
    }

    @DisplayName("끝나지 않은 게임을 찾아오는 기능 테스트")
    @Test
    void findAllOnRunning() {
        //given
        //when
        ChessGameDto first = jdbcTemplateChessDao.save(sample);
        ChessGameDto second = jdbcTemplateChessDao.save(sample);

        //then
        List<Long> onRunnings = jdbcTemplateChessDao.findAllOnRunning().stream()
                .map(ChessGameDto::getId)
                .collect(toList());

        assertThat(onRunnings).contains(first.getId(), second.getId());
    }

    @DisplayName("삭제 기능 테스트")
    @Test
    void delete() {
        //given
        ChessGameDto expectedDelete = jdbcTemplateChessDao.save(sample);

        //when
        jdbcTemplateChessDao.delete(expectedDelete.getId());
        //then
        assertThat(jdbcTemplateChessDao.findById(expectedDelete.getId()));
    }
}