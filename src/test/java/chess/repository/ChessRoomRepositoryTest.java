package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.room.Room;
import chess.model.status.Running;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ChessRoomRepositoryTest {

    private final ChessRoomRepository repository = new ChessRoomRepository(new ConnectionManager());
    private final ChessBoardRepository chessBoardRepository = new ChessBoardRepository(new ConnectionManager());
    private int boardId;
    private Room room;

    @BeforeEach
    void setup() {
        final Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        this.room = repository.save(new Room("개초보만", boardId));
    }

    @AfterEach
    void setDown() {
        repository.deleteAll();
    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(room.getTitle()).isEqualTo("개초보만"),
                () -> assertThat(room.getBoardId()).isEqualTo(boardId)
        );
    }

    @Test
    void findAll() {
        repository.save(new Room("왕허접만", boardId));
        final List<Room> boards = repository.findAllWithRunning();

        assertThat(boards.size()).isEqualTo(2);
    }
}
