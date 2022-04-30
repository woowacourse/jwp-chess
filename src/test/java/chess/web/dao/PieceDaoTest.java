package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.board.Turn;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PieceDao pieceDao;
    private RoomDao roomDao;
    private Pieces pieces;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDaoImpl(jdbcTemplate);
        roomDao = new RoomDaoImpl(jdbcTemplate);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("새로운 type과 team으로 바꾸면, db에 저장되고 다시 불러왔을 때 바뀐 type과 team이 나온다.")
    void updatePieceByPosition() {
        pieces = Pieces.createInit();
        Long id = roomDao.save(Turn.init().getTeam().value(), "title", "password");
        pieceDao.save(pieces.getPieces(), id);

        Piece piece = pieces.getPieces().get(0);
        String newType = "king";
        String newTeam = "black";

        pieceDao.updatePieceByPositionAndRoomId(newType, newTeam, piece.getPosition().name(), id);
        List<Piece> updatedPieces = pieceDao.findAllByRoomId(id);
        Piece updatedPiece = updatedPieces.get(0);

        assertThat(updatedPiece.getType()).isEqualTo(newType);
        assertThat(updatedPiece.getTeam().value()).isEqualTo(newTeam);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("초기값인 체스말 64개가 모두 조회 되야한다.")
    void findAllByRoomId() {
        pieces = Pieces.createInit();
        Long roomId = roomDao.save(Turn.init().getTeam().value(), "title", "password");
        pieceDao.save(pieces.getPieces(), roomId);

        //when
        List<Piece> pieces = pieceDao.findAllByRoomId(roomId);
        //then
        assertThat(pieces.size()).isEqualTo(64);
    }

}
