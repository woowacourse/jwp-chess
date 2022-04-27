package chess.repository;

import chess.model.board.Board;
import chess.model.member.Member;
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
class ChessMemberRepositoryTest {

    private final ChessMemberRepository chessMemberRepository;
    private final ChessBoardRepository chessBoardRepository;
    private final ChessRoomRepository chessRoomRepository;

    @Autowired
    ChessMemberRepositoryTest(JdbcTemplate jdbcTemplate) {
        chessMemberRepository = new ChessMemberRepository(jdbcTemplate);
        chessBoardRepository = new ChessBoardRepository(jdbcTemplate);
        chessRoomRepository = new ChessRoomRepository(jdbcTemplate);
    }

    private int roomId;

    @BeforeEach
    void setup() {
        final Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        final Room room = chessRoomRepository.save(new Room("초보만", board.getId()), "password");
        this.roomId = room.getId();
        chessMemberRepository.save("eden", roomId);
    }

    @Test
    void getAllByBoardId() {
        chessMemberRepository.save("corinne", roomId);
        final List<Member> members = chessMemberRepository.findMembersByRoomId(roomId);

        assertAll(
                () -> assertThat(members.get(0).getName()).isEqualTo("eden"),
                () -> assertThat(members.get(1).getName()).isEqualTo("corinne")
        );

    }

    @Test
    void save() {
        final Member member = chessMemberRepository.save("corinne", roomId);

        assertThat(member.getName()).isEqualTo("corinne");
    }

    @Test
    void saveAll() {
        List<Member> members = List.of(new Member("neo"));
        chessMemberRepository.saveAll(members, roomId);
        final List<Member> savedMembers = chessMemberRepository.findMembersByRoomId(roomId);

        assertThat(savedMembers.size()).isEqualTo(2);
    }
}
