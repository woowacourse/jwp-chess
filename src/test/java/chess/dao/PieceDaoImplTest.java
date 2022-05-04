package chess.dao;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.position.Position;
import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:pieceDaoTest.sql")
class PieceDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDaoImpl(jdbcTemplate);
        final Map<Position, Piece> pieces = Map.of(
                Position.from("e1"), new King(WHITE),
                Position.from("d1"), new Queen(WHITE),
                Position.from("e8"), new King(BLACK),
                Position.from("d8"), new Queen(BLACK)
        );
        pieceDao.saveAllPieces(1, pieces);
    }

    @Test
    @DisplayName("체스방에 해당하는 기물들을 반환한다.")
    void findPiecesByRoomIndex() {
        //given
        final int expected = 4;
        final int roomId = 1;

        //when
        final List<PieceDto> actual = pieceDao.findPieces(roomId);

        //then
        assertThat(actual).hasSize(expected);
    }

    @Test
    @DisplayName("체스 방의 기물중 넘겨받은 위치 값을 가진 기물을 삭제한다.")
    void removePiece() {
        //given
        final int roomId = 1;
        final String position = "d8";
        pieceDao.removePiece(roomId, position);

        //when
        final int count = countPieceConditionOf(1, "Queen", position, "WHITE");

        //then
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("기물 정보를 저장한다.")
    void savePiece() {
        //given
        final int roomId = 1;
        final String position = "f2";
        final Piece piece = new Pawn(WHITE);
        pieceDao.savePiece(roomId, position, piece);

        //when
        final int count = countPieceConditionOf(roomId, "Pawn", position, "WHITE");

        //then
        assertThat(count).isEqualTo(1);
    }

    private int countPieceConditionOf(final int roomId, final String name,
                                      final String position, final String teamColor) {
        return (int) pieceDao.findPieces(roomId)
                .stream()
                .filter(pieceDto -> pieceDto.getName().equals(name))
                .filter(pieceDto -> pieceDto.getPosition().equals(position))
                .filter(pieceDto -> pieceDto.getTeam().equals(teamColor))
                .count();
    }

    @Test
    @DisplayName("모든 기물 정보들을 삭제한다.")
    void removeAll() {
        //given
        pieceDao.removeAllPieces(1);

        //when
        final List<PieceDto> pieces = pieceDao.findPieces(1);

        //then
        assertThat(pieces).isEmpty();
    }
}
