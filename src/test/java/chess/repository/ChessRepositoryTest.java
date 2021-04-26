package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import dto.MoveDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ChessRepositoryTest {
    private final ChessRepository chessRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
    private final Room room = new Room(1, "멍토", "비번", 1);

    public ChessRepositoryTest(final ChessRepository chessRepository, final JdbcTemplate jdbcTemplate) {
        this.chessRepository = chessRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM team;"
            + "ALTER TABLE team ALTER COLUMN team_id RESTART WITH 1;"
            + "DELETE FROM piece;"
            + "ALTER TABLE piece ALTER COLUMN piece_id RESTART WITH 1;"
            + "DELETE FROM room;"
            + "ALTER TABLE room ALTER COLUMN room_id RESTART WITH 1;"
            + "DELETE FROM game;"
            + "ALTER TABLE game ALTER COLUMN game_id RESTART WITH 1;");
    }

    @Test
    @DisplayName("방생성 테스트")
    void createRoom() {
        assertThatCode(() -> chessRepository.createRoom(chessGame, room)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("방 정보 가져오기 테스트")
    void loadRoom() {
        chessRepository.createRoom(chessGame, room);
        Room loadRoom = chessRepository.loadRoom(1);
        assertThat(loadRoom).isEqualTo(room);
    }

    @Test
    @DisplayName("모든 방 정보 가져오기")
    void loadAllRoom() {
        chessRepository.createRoom(chessGame, room);
        Room secondRoom = new Room(2, "망토", "토끼", 2);
        chessRepository.createRoom(chessGame, secondRoom);

        List<Room> rooms = chessRepository.loadAllRoom();
        assertThat(rooms).hasSize(2);
        assertThat(rooms.get(0)).isEqualTo(room);
        assertThat(rooms.get(1)).isEqualTo(secondRoom);
    }

    @Test
    @DisplayName("게임정보 가져오기")
    void loadGame() {
        chessRepository.createRoom(chessGame, room);
        ChessGame loadChessGame = chessRepository.loadGame(1);
        assertThat(loadChessGame).isEqualTo(chessGame);
    }

    @Test
    @DisplayName("게임 저장하기 테스트")
    void saveGame() {
        chessRepository.createRoom(chessGame, room);
        ChessGame loadChessGame = chessRepository.loadGame(1);
        MoveDto moveDto = new MoveDto("a2", "a4");
        loadChessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()));
        loadChessGame.finish();
        assertThatCode(() -> chessRepository.saveGame(1, loadChessGame, moveDto)).doesNotThrowAnyException();
        ChessGame saveChessGame = chessRepository.loadGame(1);
        assertThat(loadChessGame).isEqualTo(saveChessGame);
    }
}