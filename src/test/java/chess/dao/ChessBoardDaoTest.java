package chess.dao;

import chess.domain.Team;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import chess.dto.request.GameIdRequest;
import chess.dto.response.PieceResponse;
import chess.entity.BoardEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@JdbcTest
class ChessBoardDaoTest {

    private ChessBoardDao chessBoardDao;
    private ChessRoomDao chessRoomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessBoardDao = new ChessBoardDao(jdbcTemplate);
        chessRoomDao = new ChessRoomDao(jdbcTemplate);
        jdbcTemplate.update("insert into room (id, status, name, password) values(?, ?, ?, ?)",
                1000, "WHITE", "green", "1234");

        List<Object[]> pieces = Stream.of("a7 P" + " " + roomId(), "b7 P"+ " " + roomId(),
                        "a2 p" + " " + roomId())
                .map(piece -> piece.split(" "))
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("insert into board (position, symbol, room_id) values(?, ?, ?)", pieces);
    }

    private Long roomId() {
        return chessRoomDao.findById(new GameIdRequest(1000L)).getId();
    }

    @Test
    @DisplayName("기물 불러오기 확인")
    void findAll() {
        List<BoardEntity> pieces = chessBoardDao.findAllPiece(roomId());

        assertThat(pieces).hasSize(3);
    }

    @Test
    @DisplayName("기물 저장 확인")
    void saveAll() {
        Map<Position, Piece> board = Map.of(Position.of(Column.A, Row.EIGHT), new Pawn(Team.BLACK));
        chessBoardDao.savePieces(board, roomId());

        assertThat(chessBoardDao.findAllPiece(roomId()).size()).isEqualTo(4);
    }

    @Test
    @DisplayName("기물 위치 업데이트 확인")
    void updatePosition() {
        chessBoardDao.updatePosition("a7", "a6", roomId());
        List<BoardEntity> allPiece = chessBoardDao.findAllPiece(roomId());
        long count = allPiece.stream()
                .filter(piece -> piece.getPosition().equals("a6"))
                .count();

        assertThat(count).isEqualTo(count);
    }

   @Test
   void deleteAllPiece() {
        chessBoardDao.deleteBoard(1000L);

        assertThat(chessBoardDao.findAllPiece(1000L).size()).isZero();
   }
}
