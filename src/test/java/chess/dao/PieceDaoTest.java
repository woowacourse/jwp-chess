package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;
import chess.entity.PieceEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"schema.sql", "test-data.sql"})
public class PieceDaoTest {

    private static final Long ROOM_ID = 1L;

    private PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDao(jdbcTemplate, dataSource);
        setTestData();
    }

    private void setTestData() {
        final ChessBoard chessBoard = ChessBoardFactory.createChessBoard();
        final Map<Position, ChessPiece> pieces = chessBoard.findAllPiece();
        final List<PieceEntity> pieceEntities = pieces.entrySet()
                .stream()
                .map(entry -> new PieceEntity(ROOM_ID, entry.getKey().getValue(),
                        ChessPieceMapper.toPieceType(entry.getValue()), entry.getValue().color().getValue()))
                .collect(Collectors.toList());

        pieceDao.saveAllByRoomId(pieceEntities);
    }

    @Test
    @DisplayName("방의 모든 기물을 조회한다.")
    void findAllByRoomId() {
        assertThat(pieceDao.findAllByRoomId(ROOM_ID)).hasSize(32);
    }

    @Test
    @DisplayName("방의 기물의 위치를 변경한다.")
    void updatePositionByRoomId() {
        pieceDao.updatePositionByRoomId(ROOM_ID, "b2", "b3");

        assertThat(pieceDao.findByRoomIdPosition(ROOM_ID, "b3").getType()).isEqualTo("pawn");
    }

    @Test
    @DisplayName("방의 특정 위치의 기물을 삭제한다.")
    void deleteByRoomIdAndPosition() {
        pieceDao.deleteByRoomIdAndPosition(ROOM_ID, "a1");

        assertThat(pieceDao.findAllByRoomId(ROOM_ID)).hasSize(31);
    }
}
