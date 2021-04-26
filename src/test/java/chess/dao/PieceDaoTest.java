package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import dto.MoveDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import util.PieceConverter;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PieceDaoTest {
    private static final String WHITE = "White";
    private static final String BLACK = "Black";

    private final PieceDao pieceDao;
    private final JdbcTemplate jdbcTemplate;

    public PieceDaoTest(final PieceDao pieceDao, final JdbcTemplate jdbcTemplate) {
        this.pieceDao = pieceDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("DELETE FROM team;"
            + "ALTER TABLE team ALTER COLUMN team_id RESTART WITH 1;"
            + "DELETE FROM piece;"
            + "ALTER TABLE piece ALTER COLUMN piece_id RESTART WITH 1;"
            + "DELETE FROM room;"
            + "ALTER TABLE room ALTER COLUMN room_id RESTART WITH 1;"
            + "DELETE FROM game;"
            + "ALTER TABLE game ALTER COLUMN game_id RESTART WITH 1;");

        final ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con
                .prepareStatement("insert into game (is_end) values (?)");
            preparedStatement.setBoolean(1, chessGame.isEnd());
            return preparedStatement;
        });

        Map<Position, Piece> whitePieces = chessGame.getWhiteTeam().getPiecePosition();
        List<Position> whitePositions = new ArrayList<>(whitePieces.keySet());
        jdbcTemplate.batchUpdate("insert into piece (name, color, position, game_id) values (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Piece piece = whitePieces.get(whitePositions.get(i));
                ps.setString(1, PieceConverter.convertToPieceName(WHITE, piece));
                ps.setString(2, WHITE);
                ps.setString(3, whitePositions.get(i).getKey());
                ps.setLong(4, 1);
            }
            @Override
            public int getBatchSize() {
                return whitePieces.size();
            }
        });

        Map<Position, Piece> blackPieces = chessGame.getBlackTeam().getPiecePosition();
        List<Position> blackPositions = new ArrayList<>(blackPieces.keySet());
        jdbcTemplate.batchUpdate("insert into piece (name, color, position, game_id) values (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Piece piece = blackPieces.get(blackPositions.get(i));
                ps.setString(1, PieceConverter.convertToPieceName(BLACK, piece));
                ps.setString(2, BLACK);
                ps.setString(3, blackPositions.get(i).getKey());
                ps.setLong(4, 1);
            }
            @Override
            public int getBatchSize() {
                return blackPieces.size();
            }
        });
    }

    @Test
    @DisplayName("말 생성 테스트")
    void create() {
        ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
        Map<Position, Piece> pieces = chessGame.getWhiteTeam().getPiecePosition();
        assertThatCode(() -> pieceDao.create(pieces, WHITE, 1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말 불러오기 테스트")
    void load() {
        assertThat(pieceDao.load(1, WHITE)).hasSize(16);
        assertThat(pieceDao.load(1, BLACK)).hasSize(16);
    }

    @Test
    @DisplayName("말 삭제 테스트")
    void delete() {
        MoveDto moveDto = new MoveDto("a2", "a7");
        pieceDao.delete(1, moveDto);
        Map<Position, Piece> pieces = pieceDao.load(1, BLACK);
        assertThat(pieces).hasSize(15);
    }

    @Test
    @DisplayName("말 수정 테스트")
    void update() {
        MoveDto moveDto = new MoveDto("a2", "a4");
        pieceDao.update(1, moveDto);
        Map<Position, Piece> pieces = pieceDao.load(1, WHITE);
        assertThat(pieces.get(Position.of(moveDto.getTo()))).isInstanceOf(Pawn.class);
    }
}