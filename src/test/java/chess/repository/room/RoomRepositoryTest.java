package chess.repository.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.board.Board;
import chess.domain.game.Room;
import chess.domain.gamestate.running.Move;
import chess.domain.gamestate.running.Ready;
import chess.domain.location.Location;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.utils.BoardUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RoomRepositoryTest {

    @Autowired
    RoomRepositoryImpl roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
    }

    @Test
    void insert_and_findRoomByName() {
        // given
        String name = "RoomRepo insert테스트";

        // when
        long roomId = roomRepository.insert(name);
        Room insertedRoom = roomRepository.findRoomByName(name);

        // then
        assertAll(
            () -> assertThat(insertedRoom.getId()).isEqualTo(roomId),
            () -> assertThat(insertedRoom.getName()).isEqualTo(name)
        );
    }

    @DisplayName("save - 방의 상태를 변경할 수 있습니다. (move명령이 아닌 경우에만 해당합니다.)")
    @Test
    void save() {
        // given
        String name = "RoomRepo save테스트";
        long roomId = roomRepository.insert(name);
        Room room = roomRepository.findRoomByName(name);

        // when
        room.play("start");
        roomRepository.save(room);

        // then
        Room savedRoom = roomRepository.findRoomByName(name);
        assertAll(
            () -> assertThat(savedRoom.getId()).isEqualTo(roomId),
            () -> assertThat(savedRoom.getName()).isEqualTo(name),
            () -> assertThat(savedRoom.getState().getValue()).isEqualTo("start")
        );
    }

    @DisplayName("save - 방의 상태와 방에 소속된 체스 기물들의 정보를 변경할 수 있습니다. (move명령인 경우에만 해당합니다.)")
    @Test
    void saveAfterMove() {
        // given
        char[][] TEST_BOARD_VALUE = {
            {'R', 'N', 'B', 'Q', 'K', 'B', 'N', '.'},
            {'P', 'P', 'P', 'P', 'P', 'P', 'P', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', 'R'},
            {'.', '.', '.', '.', '.', '.', 'p', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'p', 'p', 'p', 'p', 'p', 'p', '.', 'p'},
            {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}
        };
        String name = "RoomRepo saveAfterMove테스트";
        Board board = BoardUtil.convertToBoard(TEST_BOARD_VALUE);
        String source = "74";
        String target = "85";
        Room initialRoom = new Room(0,
            name,
            new Move(board),
            Team.WHITE
        );
        long roomId = roomRepository.insert(initialRoom);

        // when
        Room room = roomRepository.findRoomByName(name);
        room.play("move " + source + " " + target);
        roomRepository.saveAfterMove(room, source, target);

        // then
        Room resultRoom = roomRepository.findRoomByName(name);
        Board resultBoard = resultRoom.getBoard();
        Piece pieceAtTarget = resultBoard.find(Location.of(target));
        assertAll(
            () -> assertThat(resultRoom.getId()).isEqualTo(roomId),
            () -> assertThat(resultRoom.getName()).isEqualTo(name),
            () -> assertThat(resultRoom.getState().getValue()).isEqualTo("move"),
            () -> assertThat(pieceAtTarget.getSignature()).isEqualTo('p'),
            () -> assertThat(pieceAtTarget.getTeam()).isEqualTo(Team.WHITE)
        );
    }
}
