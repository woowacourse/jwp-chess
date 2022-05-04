package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.piece.Piece;
import chess.domain.piece.role.Pawn;
import chess.domain.position.Position;
import chess.domain.GameState;
import chess.repository.entity.PieceEntity;
import chess.repository.entity.RoomEntity;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
class PieceDaoImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private PieceDao pieceDao;

    private final Board board = new Board(new RegularRuleSetup());
    private int boardId;

    @BeforeEach
    void init() {
        BoardDao boardDao = new BoardDaoImpl(dataSource, jdbcTemplate);
        RoomDao roomDao = new RoomDaoImpl(dataSource, jdbcTemplate);
        int roomId = roomDao.save(new RoomEntity("summer", "summer"));
        boardId = boardDao.save(roomId, GameState.from(board));

        pieceDao = new PieceDaoImpl(dataSource, jdbcTemplate);
    }

    @Test
    @DisplayName("Piece 저장")
    void savePiece() {
        int pieceId = pieceDao.save(boardId, "a1",
            PieceEntity.from(Position.of("a1"), new Piece(Color.WHITE, new Pawn())));
        assertThat(pieceId).isGreaterThan(0);
    }

    @Test
    @DisplayName("여러 piece 저장")
    void saveAll() {
        pieceDao.saveAll(boardId, board.getPieces());
        Integer count = jdbcTemplate.queryForObject(
            "select count(*) from piece where board_id = :boardId",
            Map.of("boardId", this.boardId),
            Integer.class);
        assertThat(count).isEqualTo(board.getPieces().size());
    }

    @Test
    @DisplayName("피스 1개 조회")
    void findOne() {
        pieceDao.save(boardId, "a2", new PieceEntity("a1", "WHITE", "q"));
        PieceEntity findPiece = pieceDao.findOne(boardId, "a2")
            .orElseThrow(NoSuchElementException::new);

        assertThat(findPiece).isEqualTo(new PieceEntity("a2", "WHITE", "q"));
    }

    @Test
    @DisplayName("피스 전체 조회")
    void findAll() {
        Map<Position, Piece> pieces = board.getPieces();
        pieceDao.saveAll(boardId, pieces);

        List<PieceEntity> findPieces = pieceDao.findAll(boardId);

        assertThat(pieces.size()).isEqualTo(findPieces.size());
    }

    @Test
    @DisplayName("피스 단일 업데이트")
    void updateOne() {
        pieceDao.save(boardId, "a1", new PieceEntity("a1", "BLACK", "q"));
        PieceEntity updatePiece = new PieceEntity("c3", "WHITE", "q");
        pieceDao.save(boardId, "c3", updatePiece);
        pieceDao.updateOne(boardId, "a1", updatePiece);
        PieceEntity findPiece = pieceDao.findOne(boardId, "a1").orElseThrow();
        assertThat(updatePiece).isEqualTo(findPiece);
    }

    @Test
    @DisplayName("피스를 삭제한다")
    void delete() {
        pieceDao.save(boardId, "a1", new PieceEntity("a1", "BLACK", "q"));
        pieceDao.deleteOne(boardId, "a1");
        Optional<PieceEntity> findPiece = pieceDao.findOne(boardId, "a1");
        assertThat(findPiece).isEmpty();
    }
}