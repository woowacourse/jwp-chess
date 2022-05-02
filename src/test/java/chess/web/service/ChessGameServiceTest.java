package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import chess.domain.game.ChessGame;
import chess.domain.game.state.Player;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.position.Position;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.ChessBoardDaoImpl;
import chess.web.dao.RoomDao;
import chess.web.dao.RoomDaoImpl;
import chess.web.dto.ChessCellDto;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.CreateRoomResultDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ChessGameServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessBoardDao chessBoardDao;

    @MockBean
    private RoomDao roomDao;

    @Autowired
    ChessGameService chessGameService;

    RoomDto roomDto;
    Map<Position, Piece> board = new HashMap<>();

    @BeforeEach
    void setUp() {
        board.put(Position.of("A2"), PieceFactory.PAWN_WHITE.getPiece());
        roomDto = new RoomDto(1, "testTitle", false, false);

        given(roomDao.isStartable(1)).willReturn(roomDto);
        given(chessBoardDao.findAllPieces(1)).willReturn(board);
        given(roomDao.getPlayer(1)).willReturn(Player.WHITE);

    }

    @Test
    void start_roomExist() {
        RoomDto roomDto = new RoomDto(1, "testTitle", false, false);
        given(chessBoardDao.boardExistInRoom(1)).willReturn(true);
        given(roomDao.isStartable(1)).willReturn(roomDto);

        RoomDto roomDtoResult = chessGameService.start(1);
        assertThat(roomDtoResult).isEqualTo(roomDto);
    }

    @Test
    void start_roomNotExist() {
        RoomDto roomDto = new RoomDto(1, "testTitle", false, false);
        given(chessBoardDao.boardExistInRoom(1)).willReturn(false);
        given(roomDao.isStartable(1)).willReturn(roomDto);

        RoomDto roomDtoResult = chessGameService.start(1);
        assertThat(roomDtoResult).isNotNull();
    }

    @Test
    void move_game_not_end() {
        MoveResultDto moveResultDto = chessGameService.move(new MoveDto("A2", "A4"), 1);
        assertThat(moveResultDto).isNotNull();
    }

    @Test
    void move_game_end() {
        board.put(Position.of("E7"), PieceFactory.KING_BLACK.getPiece());
        board.put(Position.of("D6"), PieceFactory.PAWN_WHITE.getPiece());

        given(chessBoardDao.findAllPieces(1)).willReturn(board);

        MoveResultDto moveResultDto = chessGameService.move(new MoveDto("D6", "E7"), 1);
        assertThat(moveResultDto.getIsGameOver()).isTrue();
    }

    @Test
    void play() {
        PlayResultDto playResultDto = chessGameService.play(1);

        assertThat(playResultDto.getBoard()).isNotNull();
    }

    @Test
    void getChessGame() {
        ChessGame chessGame = chessGameService.getChessGame(1);
        assertThat(chessGame.getBoard()).isNotNull();
    }

    @Test
    void createRoom() {
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto("testTitle", "testPassword");
        CreateRoomResultDto room = chessGameService.createRoom(createRoomRequestDto);
        assertThat(room).isNotNull();
    }

    @Test
    void findAllRooms() {
        ReadRoomResultDto readRoomResultDto = new ReadRoomResultDto(List.of(new RoomDto()));
        given(roomDao.findAll()).willReturn(readRoomResultDto);
        ReadRoomResultDto findAllRoomsResult = chessGameService.findAllRooms();
        assertThat(findAllRoomsResult.getRooms().size()).isEqualTo(1);
    }

    @Test
    void delete() {
        DeleteDto deleteDto = new DeleteDto("testPassword");
        DeleteResultDto deleteResultDto = new DeleteResultDto(1, true);

        given(roomDao.delete(1, deleteDto)).willReturn(deleteResultDto);

        DeleteResultDto deleteResult = chessGameService.delete(1, deleteDto);
        assertThat(deleteResult.isDeleted()).isEqualTo(true);
    }
}
