package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.SquareEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class SquareDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SquareDaoImpl squareDao;

    @BeforeEach
    void beforeEach() {
        squareDao = new SquareDaoImpl(jdbcTemplate);
        JdbcFixture.dropTable(jdbcTemplate, "square");
        JdbcFixture.dropTable(jdbcTemplate, "room");

        JdbcFixture.createRoomTable(jdbcTemplate);
        JdbcFixture.createSquareTable(jdbcTemplate);

        JdbcFixture.insertRoom(jdbcTemplate, "sojukang", "1234", "white");
        JdbcFixture.insertSquares(jdbcTemplate, List.of(
                "a1,white_pawn,1",
                "a2,black_pawn,1",
                "a3,white_queen,1"));
    }

    @Test
    @DisplayName("Sqaure 들을 저장한다.")
    void saveAll() {
        List<SquareEntity> squares = List.of(
                new SquareEntity("b1", "white_pawn"),
                new SquareEntity("b2", "white_pawn")
        );
        squareDao.saveAll(squares, 1);

        List<SquareEntity> result = squareDao.findByRoomId(1);
        assertThat(result).hasSize(5);
    }

    @Test
    @DisplayName("roomId와 Position을 이용해 Square를 얻는다.")
    void findByRoomIdAndPosition() {
        SquareEntity square = squareDao.findByRoomIdAndPosition(1, "a1").get();
        assertThat(square.getPiece()).isEqualTo("white_pawn");
    }

    @Test
    @DisplayName("square를 업데이트 할 수 있다.")
    void update() {
        squareDao.update(1, "a1", "empty");
        SquareEntity square = squareDao.findByRoomIdAndPosition(1, "a1").get();
        assertThat(square.getPiece()).isEqualTo("empty");
    }

    @Test
    @DisplayName("RoomId로 square를 얻을 수 있다.")
    void findByRoomId() {
        List<SquareEntity> squares = squareDao.findByRoomId(1);
        assertThat(squares.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 RoomId를 참조한 모든 Square를 제거할 수 있다.")
    void removeAll() {
        JdbcFixture.insertRoom(jdbcTemplate, "test", "", "white");
        JdbcFixture.insertSquares(jdbcTemplate, List.of("b1,white_pawn,2"));

        squareDao.removeAll(2);
        List<SquareEntity> squares = squareDao.findByRoomId(2);
        assertThat(squares.isEmpty()).isTrue();
    }
}
