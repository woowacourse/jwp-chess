package chess.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.entity.Square;

@JdbcTest
class SquareDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SquareDaoImpl squareDao;
    private JdbcFixture jdbcFixture;
    private final long roomId = 1L;

    @BeforeEach
    void beforeEach() {
        jdbcFixture = new JdbcFixture(jdbcTemplate);
        squareDao = new SquareDaoImpl(jdbcTemplate);

        jdbcFixture.dropTable("square");
        jdbcFixture.dropTable("room");

        jdbcFixture.createRoomTable();
        jdbcFixture.createSquareTable();

        jdbcFixture.insertRoom("sojukang", "white", "pw");
        jdbcFixture.insertSquares(List.of(
            "a1,white_pawn,1",
            "a2,black_pawn,1",
            "a3,white_queen,1"));
    }

    @Test
    @DisplayName("Sqaure 들을 저장한다.")
    void saveAll() {
        List<Square> squares = List.of(
            new Square("b1", "white_pawn"),
            new Square("b2", "white_pawn")
        );
        squareDao.saveAll(squares, roomId);

        List<Square> result = squareDao.findByRoomId(roomId);
        assertThat(result).hasSize(5);
    }

    @Test
    @DisplayName("roomId와 Position을 이용해 Square를 얻는다.")
    void findByRoomIdAndPosition() {
        Square square = squareDao.findByRoomIdAndPosition(roomId, "a1").get();
        assertThat(square.getPiece()).isEqualTo("white_pawn");
    }

    @Test
    @DisplayName("square를 업데이트한다.")
    void update() {
        squareDao.update(roomId, "a1", "empty");
        Square square = squareDao.findByRoomIdAndPosition(roomId, "a1").get();
        assertThat(square.getPiece()).isEqualTo("empty");
    }

    @Test
    @DisplayName("RoomId로 square를 얻는다.")
    void findByRoomId() {
        List<Square> squares = squareDao.findByRoomId(roomId);
        assertThat(squares.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 RoomId를 참조한 모든 Square를 제거한다.")
    void removeAll() {
        jdbcFixture.insertRoom("test", "white", "pw");
        jdbcFixture.insertSquares(List.of("b1,white_pawn,2"));

        squareDao.removeAll(roomId + 1);
        List<Square> squares = squareDao.findByRoomId(roomId + 1);
        assertThat(squares.isEmpty()).isTrue();
    }
}
