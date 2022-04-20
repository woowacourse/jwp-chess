package chess.dao;

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

    private final ChessRoomDao dao = new ChessRoomDao(new ConnectionManager());
    private final ChessBoardDao chessBoardDao = new ChessBoardDao(new ConnectionManager());
    private int boardId;
    private Room room;

    @BeforeEach
    void setup() {
        final Board board = chessBoardDao.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        this.room = dao.save(new Room("개초보만", boardId));
    }

    @AfterEach
    void setDown() {
        dao.deleteAll();
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
        dao.save(new Room("왕허접만", boardId));
        final List<Room> boards = dao.findAllWithRunning();

        assertThat(boards.size()).isEqualTo(2);
    }
}
