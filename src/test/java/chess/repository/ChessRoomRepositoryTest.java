package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.room.Room;
import chess.model.status.Running;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ChessRoomRepositoryTest {

    @Autowired
    private ChessRoomRepository chessRoomRepository;

    @Autowired
    private ChessBoardRepository chessBoardRepository;
    private int boardId;
    private Room room;

    @BeforeEach
    void setup() {
        final Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        this.boardId = board.getId();
        this.room = chessRoomRepository.save(new Room("개초보만", boardId));
    }

//    @AfterEach
//    void setDown() {
//        repository.deleteAll();
//    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(room.getTitle()).isEqualTo("개초보만"),
                () -> assertThat(room.getBoardId()).isEqualTo(boardId)
        );
    }

    @Test
    void findAllByStatus() {
        chessRoomRepository.save(new Room("왕허접만", boardId));
        final List<Room> rooms = chessRoomRepository.findAllByBoardStatus(new Running());
        assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    void getRoomById() {
        Room room = chessRoomRepository.getById(this.room.getId());
        assertThat(room.getTitle()).isEqualTo("개초보만");
    }
}
