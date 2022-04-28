package chess.dao;

import static chess.domain.GameStatus.READY;
import static chess.domain.chesspiece.Color.WHITE;
import static chess.domain.position.File.A;
import static chess.domain.position.File.B;
import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.Rank.TWO;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Pawn;
import chess.domain.position.Position;
import chess.entity.ChessPieceEntity;
import chess.entity.RoomEntity;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcChessPieceDaoTest {

    private static final RoomEntity ROOM_ENTITY = new RoomEntity(0, "매트의 체스", "123123",
            READY.getValue(), WHITE.getValue());

    private static final Map<Position, ChessPiece> PIECES_BY_POSITION = Map.of(
            Position.of(A, TWO), Pawn.from(WHITE),
            Position.of(B, TWO), Pawn.from(WHITE),
            Position.of(C, TWO), Pawn.from(WHITE),
            Position.of(D, TWO), Pawn.from(WHITE)
    );

    private static int ROOM_ID;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcChessPieceDao chessPieceDao;
    private JdbcRoomDao roomDao;

    @BeforeEach
    void setUp() {
        chessPieceDao = new JdbcChessPieceDao(jdbcTemplate);
        roomDao = new JdbcRoomDao(jdbcTemplate);

        ROOM_ID = roomDao.save(ROOM_ENTITY);
    }

    @DisplayName("chess piece들을 저장한다.")
    @Test
    void chess_pieces_저장한다() {
        chessPieceDao.saveAll(ROOM_ID, PIECES_BY_POSITION);

        final List<ChessPieceEntity> chessPieces = chessPieceDao.findByRoomId(ROOM_ID);
        chessPieces.forEach(System.out::println);
        assertThat(chessPieces.size()).isEqualTo(4);
    }

    @DisplayName("chess piece를 수정한다.")
    @Test
    void chess_piece_수정한다() {
        chessPieceDao.saveAll(ROOM_ID, PIECES_BY_POSITION);

        chessPieceDao.update(ROOM_ID, "a2", "a4");

        final boolean result = chessPieceDao.findByRoomId(ROOM_ID)
                .stream()
                .anyMatch(chessPieceEntity -> chessPieceEntity.getPosition().equals("a4"));
        assertThat(result).isTrue();
    }

    @DisplayName("room id와 position을 기반으로 기물을 삭제한다.")
    @Test
    void 기물_삭제한다() {
        chessPieceDao.saveAll(ROOM_ID, PIECES_BY_POSITION);

        chessPieceDao.deleteByRoomIdAndPosition(ROOM_ID, "a2");

        assertThat(chessPieceDao.findByRoomId(ROOM_ID).size()).isEqualTo(3);
    }

    @DisplayName("room id를 기반으로 기물을 모두 삭제한다.")
    @Test
    void 기물_전부_삭제한다() {
        chessPieceDao.saveAll(ROOM_ID, PIECES_BY_POSITION);

        chessPieceDao.deleteByRoomId(ROOM_ID);

        assertThat(chessPieceDao.findByRoomId(ROOM_ID).size()).isEqualTo(0);
    }
}
