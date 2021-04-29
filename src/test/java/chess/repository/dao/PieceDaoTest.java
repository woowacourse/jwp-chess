package chess.repository.dao;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.repository.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PieceDaoTest {
    private PieceDao pieceDao;
    private ChessGameManager chessGameManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        this.pieceDao = new PieceDao(this.jdbcTemplate);
        this.chessGameManager = new ChessGameManager();
        this.chessGameManager.start();
    }

    @Test
    @DisplayName("진행된 게임에서 체스 보드를 읽어온다.")
    void findChessBoardByGameIdTest() {
        // given
        long gameId = gameRepository.save(chessGameManager, "test title");

        // when
        pieceDao.savePieces(chessGameManager, gameId);
        ChessBoard chessBoardFound = this.pieceDao.findChessBoardByGameId(gameId);

        // then
        assertThat(chessBoardFound).isEqualTo(chessGameManager.getBoard());
    }

    @Test
    @DisplayName("진행되고 있는 게임의 모든 기물을 저장한다.")
    void savePiecesTest() {
        // given
        long gameId = gameRepository.save(chessGameManager, "test title");

        // when
        pieceDao.savePieces(chessGameManager, gameId);

        //then
        Integer rowFound = this.jdbcTemplate.queryForObject("SELECT count(*) FROM piece WHERE game_id = " + Long.toString(gameId), Integer.class);
        assertThat(rowFound).isEqualTo(32);
    }

    @Test
    @DisplayName("지정된 위치의 기물을 읽어온다.")
    void findPieceByPositionTest() {
        // given
        long gameId = gameRepository.save(chessGameManager, "test title");
        pieceDao.savePieces(chessGameManager, gameId);
        Position position = Position.of("a2");  // White pawn

        // when
        Piece pieceFound = pieceDao.findPieceByPosition(position, gameId);

        // then
        assertThat(pieceFound).isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("지정된 위치의 기물을 삭제한다.")
    void deletePieceByPositionTest() {
        // given
        long gameId = gameRepository.save(chessGameManager, "test title"); // to foreignKey
        Position position = Position.of("a2");

        // when
        pieceDao.deletePieceByPosition(position, gameId);

        // then
        assertThatThrownBy(() -> this.pieceDao.findPieceByPosition(position, gameId))
                .isInstanceOf(EmptyResultDataAccessException.class);

    }

    @AfterEach
    void flush() {
        this.jdbcTemplate.execute("DELETE FROM game");
    }
}