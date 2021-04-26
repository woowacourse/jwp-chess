package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import dto.ChessGameDto;
import dto.MoveDto;
import dto.PieceDto;
import dto.RoomDto;
import dto.TeamDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ChessServiceTest {
    private final ChessService chessService;
    private final JdbcTemplate jdbcTemplate;
    private final ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());
    private final Room room = new Room(1, "멍토", "비번", 1);

    public ChessServiceTest(final ChessService chessService, final JdbcTemplate jdbcTemplate) {
        this.chessService = chessService;
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
    @DisplayName("방 생성 테스트")
    void createRoom() {
        assertThatCode(() -> chessService.createRoom(room)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("모든 방 조회 테스트")
    void loadAllRoom() {
        createRoom();
        Room secondRoom = new Room(2, "망토", "토끼", 2);
        chessService.createRoom(secondRoom);

        List<RoomDto> roomDtos = chessService.loadAllRoom();
        assertThat(roomDtos).hasSize(2);
        assertThatCode(() -> {
            RoomDto roomDto = roomDtos.get(0);
            assertThat(roomDto.getName()).isEqualTo(room.getName());
            assertThat(roomDto.getGameId()).isEqualTo(room.getGameId());
        }).doesNotThrowAnyException();

        assertThatCode(() -> {
            RoomDto roomDto = roomDtos.get(1);
            assertThat(roomDto.getName()).isEqualTo(secondRoom.getName());
            assertThat(roomDto.getGameId()).isEqualTo(secondRoom.getGameId());
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 불러오기 테스트")
    void loadGame() {
        createRoom();
        ChessGameDto chessGameDto = chessService.loadGame(1, room);
        assertThat(chessGameDto.isEnd()).isFalse();
        assertThatCode(() -> {
            TeamDto whiteTeam = chessGameDto.getWhiteTeam();
            assertThat(whiteTeam.getName()).isEqualTo("White");
            assertThat(whiteTeam.isTurn()).isTrue();
            assertThat(whiteTeam.getScore()).isEqualTo(38.0);

            List<PieceDto> whiteTeamPieces = whiteTeam.getPieces().getPieces();
            assertThat(whiteTeamPieces).hasSize(16);
        }).doesNotThrowAnyException();

        assertThatCode(() -> {
            TeamDto blackTeam = chessGameDto.getBlackTeam();
            assertThat(blackTeam.getName()).isEqualTo("Black");
            assertThat(blackTeam.isTurn()).isFalse();
            assertThat(blackTeam.getScore()).isEqualTo(38.0);

            List<PieceDto> whiteTeamPieces = blackTeam.getPieces().getPieces();
            assertThat(whiteTeamPieces).hasSize(16);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말 이동 테스트")
    void movePiece() {
        createRoom();
        MoveDto moveDto = new MoveDto("a2", "a4");
        assertThatCode(() -> chessService.movePiece(1, moveDto)).doesNotThrowAnyException();
        ChessGameDto chessGameDto = chessService.loadGame(1, room);
        assertThatCode(() -> {
            List<PieceDto> pieces = chessGameDto.getWhiteTeam().getPieces().getPieces();
            boolean a4 = pieces.stream().
                anyMatch(pieceDto -> pieceDto.getPosition().equals("a4"));
            assertThat(a4).isTrue();

            boolean a2 = pieces.stream().
                anyMatch(pieceDto -> pieceDto.getPosition().equals("a2"));
            assertThat(a2).isFalse();
        }).doesNotThrowAnyException();

        assertThatThrownBy(() -> chessService.movePiece(1, moveDto))
            .isInstanceOf(IllegalArgumentException.class);
    }
}