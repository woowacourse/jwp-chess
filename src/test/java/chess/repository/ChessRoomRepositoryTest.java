package chess.repository;

import chess.dto.RoomContentDto;
import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.room.Room;
import chess.model.status.Running;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJdbcTest
@Sql("test-schema.sql")
class ChessRoomRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ChessRoomRepository chessRoomRepository;

    private ChessBoardRepository chessBoardRepository;
    private int boardId;
    private Room room;

    @BeforeEach
    void setup() {
        this.chessRoomRepository = new ChessRoomRepository(jdbcTemplate);
        this.chessBoardRepository = new ChessBoardRepository(jdbcTemplate);
        final Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        this.room = chessRoomRepository.save(new Room("개초보만", boardId), "password");
    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(room.getTitle()).isEqualTo("개초보만"),
                () -> assertThat(room.getBoardId()).isEqualTo(boardId)
        );
    }

    @Test
    void findAllByStatus() {
        chessRoomRepository.save(new Room("왕허접만", boardId), "password");
        final List<RoomContentDto> rooms = chessRoomRepository.findAll();
        assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    void getRoomById() {
        Room room = chessRoomRepository.getById(this.room.getId());
        assertThat(room.getTitle()).isEqualTo("개초보만");
    }
}
