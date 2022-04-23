package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.piece.Piece;
import chess.domain.piece.role.Pawn;
import chess.domain.position.Position;
import chess.web.dto.GameStateDto;
import chess.web.dto.PieceDto;
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
class PieceRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private PieceRepository pieceRepository;

    private final Board board = new Board(new RegularRuleSetup());
    private int roomId;
    private int boardId;

    @BeforeEach
    void init() {
        BoardRepository boardRepository = new BoardRepositoryImpl(dataSource, jdbcTemplate);
        RoomRepository roomRepository = new RoomRepositoryImpl(dataSource, jdbcTemplate);
        roomId = roomRepository.save("summer");
        boardId = boardRepository.save(roomId, GameStateDto.from(board));

        pieceRepository = new PieceRepositoryImpl(dataSource, jdbcTemplate);
    }

    @Test
    @DisplayName("Piece 저장")
    void savePiece() {
        int pieceId = pieceRepository.save(boardId, "a1",
                PieceDto.from(Position.of("a1"), new Piece(Color.WHITE, new Pawn())));
        assertThat(pieceId).isGreaterThan(0);
    }

    @Test
    @DisplayName("여러 piece 저장")
    void saveAll() {
        pieceRepository.saveAll(boardId, board.getPieces());
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from piece where board_id = :boardId",
                Map.of("boardId", this.boardId),
                Integer.class);
        assertThat(count).isEqualTo(board.getPieces().size());
    }

    @Test
    @DisplayName("피스 1개 조회")
    void findOne() {
        pieceRepository.save(boardId, "a2", new PieceDto("a1", "WHITE", "q"));
        PieceDto findPiece = pieceRepository.findOne(boardId, "a2")
                .orElseThrow(NoSuchElementException::new);

        assertThat(findPiece).isEqualTo(new PieceDto("a2", "WHITE", "q"));
    }

    @Test
    @DisplayName("피스 전체 조회")
    void findAll() {
        Map<Position, Piece> pieces = board.getPieces();
        pieceRepository.saveAll(boardId, pieces);

        List<PieceDto> findPieces = pieceRepository.findAll(boardId);

        assertThat(pieces.size()).isEqualTo(findPieces.size());
    }

    @Test
    @DisplayName("피스 단일 업데이트")
    void updateOne() {
        pieceRepository.save(boardId, "a1", new PieceDto("a1", "BLACK", "q"));
        PieceDto updatePiece = new PieceDto("c3", "WHITE", "q");
        pieceRepository.save(boardId, "c3", updatePiece);
        pieceRepository.updateOne(boardId, "a1", updatePiece);
        PieceDto findPiece = pieceRepository.findOne(boardId, "a1").orElseThrow();
        assertThat(updatePiece).isEqualTo(findPiece);
    }

    @Test
    @DisplayName("피스를 삭제한다")
    void delete() {
        pieceRepository.save(boardId, "a1", new PieceDto("a1", "BLACK", "q"));
        pieceRepository.deleteOne(boardId, "a1");
        Optional<PieceDto> findPiece = pieceRepository.findOne(boardId, "a1");
        assertThat(findPiece).isEmpty();
    }
}