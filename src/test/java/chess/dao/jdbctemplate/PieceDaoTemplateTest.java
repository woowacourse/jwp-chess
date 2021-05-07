package chess.dao.jdbctemplate;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.dto.game.GameDto;
import chess.dao.dto.piece.PieceDto;
import chess.domain.board.BoardInitializer;
import chess.domain.board.position.Position;
import chess.domain.board.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DataJdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("classpath:application-test.properties")
class PieceDaoTemplateTest {

    private JdbcTemplate jdbcTemplate;
    private PieceDao pieceDao;
    private GameDao gameDao;

    public PieceDaoTemplateTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        pieceDao = new PieceDaoTemplate(jdbcTemplate);
        gameDao = new GameDaoTemplate(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        Long newGameId = gameDao.save(new GameDto("게임1", "흰색유저1", "흑색유저1"));
        String sql = "INSERT INTO piece(game_id, position, symbol) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, newGameId, "a1", "r");
        jdbcTemplate.update(sql, newGameId, "a2", "n");
        jdbcTemplate.update(sql, newGameId, "a3", "b");
        jdbcTemplate.execute("COMMIT");
    }

    @Test
    void savePieces() {
        //given
        Map<Position, Piece> piecesMap = BoardInitializer.initiateBoard().getBoard();
        Long gameId = 1L;
        List<PieceDto> pieces = piecesMap.entrySet().stream()
                .map(entry -> new PieceDto(gameId, entry.getKey().parseString(), entry.getValue().getSymbol()))
                .collect(Collectors.toList());

        //when
        long[] ints = pieceDao.savePieces(gameId, pieces);

        //then
        assertThat(ints).hasSize(64);
    }

    @Test
    void updateByGameIdAndPosition() {
        //given
        String sourcePosition = "a1";
        String emptySymbol = ".";
        Long gameId = 1L;
        PieceDto pieceDto = new PieceDto(gameId, emptySymbol, sourcePosition);

        //when
        Long updateColumns = pieceDao.updateByGameIdAndPosition(pieceDto);

        //then
        assertThat(updateColumns).isEqualTo(Long.valueOf(1L));
    }

    @Test
    void findPiecesByGameId() {
        //given
        Long gameId = 1L;
        List<PieceDto> pieceResponseDtos = Arrays.asList(
                new PieceDto(gameId, "r", "a1"),
                new PieceDto(gameId, "n", "a2"),
                new PieceDto(gameId, "b", "a3")
        );

        //when
        List<PieceDto> findPieces = pieceDao.findPiecesByGameId(gameId);

        //then
        for (int i = 0; i < findPieces.size(); i++) {
            assertThat(findPieces.get(i).getSymbol()).isEqualTo(pieceResponseDtos.get(i).getSymbol());
            assertThat(findPieces.get(i).getPosition()).isEqualTo(pieceResponseDtos.get(i).getPosition());
        }
    }
}