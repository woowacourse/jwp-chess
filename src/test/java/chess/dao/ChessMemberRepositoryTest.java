package chess.dao;

import chess.model.board.Board;
import chess.model.member.Member;
import chess.model.piece.Team;
import chess.model.room.Room;
import chess.model.status.Running;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ChessMemberRepositoryTest {

    private final ChessMemberDao dao = new ChessMemberDao(new ConnectionManager());
    private final ChessBoardDao chessBoardDao = new ChessBoardDao(new ConnectionManager());
    private final ChessRoomDao chessRoomDao = new ChessRoomDao(new ConnectionManager());
    private int roomId;

    @BeforeEach
    void setup() {
        final Board board = chessBoardDao.save(new Board(new Running(), Team.WHITE));
        final Room room = chessRoomDao.save(new Room("초보만", board.getId()));
        this.roomId = room.getId();
        dao.save("eden", roomId);
    }

    @AfterEach
    void setDown() {
        chessBoardDao.deleteAll();
    }

    @Test
    void getAllByBoardId() {
        dao.save("corinne", roomId);
        final List<Member> members = dao.getAllByRoomId(roomId);

        assertAll(
                () -> assertThat(members.get(0).getName()).isEqualTo("eden"),
                () -> assertThat(members.get(1).getName()).isEqualTo("corinne")
        );

    }

    @Test
    void save() {
        final Member member = dao.save("corinne", roomId);

        assertThat(member.getName()).isEqualTo("corinne");
    }

    @Test
    void saveAll() {
        List<Member> members = List.of(new Member("neo"));
        dao.saveAll(members, roomId);
        final List<Member> savedMembers = dao.getAllByRoomId(roomId);

        assertThat(savedMembers.size()).isEqualTo(2);
    }
}
