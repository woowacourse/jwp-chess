package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.team.PiecePositions;
import chess.domain.team.Team;
import chess.webdao.BoardDao;
import chess.webdao.RoomDao;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TurnDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpringChessServiceTest {
    private static ObjectMapper objectMapper;
    @InjectMocks
    SpringChessService springChessService;
    @Mock
    BoardDao boardDao;
    @Mock
    RoomDao roomDao;
    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("새로운 게임 시작하기")
    void startNewGame() throws Exception {
        ChessGameDto expected = new ChessGameDto(new ChessGame(Team.blackTeam(), Team.whiteTeam()));

        ChessGameDto result = springChessService.startNewGame();

        assertThat(objectMapper.writeValueAsString(result)).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @Test
    @DisplayName("움직인 경우 테스트 - Rook a2 -> a3")
    void move() throws Exception {
        // given
        MoveRequestDto moveRequestDto = new MoveRequestDto();
        moveRequestDto.setStart("a2");
        moveRequestDto.setDestination("a3");

        TurnDto turnDto = new TurnDto();
        turnDto.setTurn("white");
        turnDto.setIsPlaying(true);
        given(roomDao.selectTurnByRoomId(1L)).willReturn(turnDto);

        List<BoardInfosDto> boards = new ArrayList<>();
        BoardInfosDto boardInfosDto = new BoardInfosDto();
        boardInfosDto.setIsFirstMoved(true);
        boardInfosDto.setPiece("Rook");
        boardInfosDto.setPosition("a2");
        boardInfosDto.setTeam("white");
        boards.add(boardInfosDto);
        given(boardDao.selectBoardInfosByRoomId(1L)).willReturn(boards);

        Map<Position, Piece> setting = new HashMap<>();
        setting.put(Position.of("a2"), new Rook());
        Team whiteTeam = new Team(new PiecePositions(setting));
        ChessGame chessGame = new ChessGame(new Team(new PiecePositions(Collections.emptyMap())), whiteTeam, whiteTeam, true);
        chessGame.move(Position.of("a2"), Position.of("a3"));
        ChessGameDto expected = new ChessGameDto(chessGame);

        // when
        ChessGameDto result = springChessService.move(moveRequestDto);

        // then
        assertThat(objectMapper.writeValueAsString(result)).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @Test
    @DisplayName("기존 게임 로드")
    void loadPreviousGame() throws Exception {
        // given
        Map<Position, Piece> setting = new HashMap<>();
        setting.put(Position.of("a2"), new Rook());
        Team whiteTeam = new Team(new PiecePositions(setting));
        ChessGame chessGame = new ChessGame(new Team(new PiecePositions(Collections.emptyMap())), whiteTeam, whiteTeam, true);
        ChessGameDto expected = new ChessGameDto(chessGame);

        TurnDto turnDto = new TurnDto();
        turnDto.setTurn("white");
        turnDto.setIsPlaying(true);
        given(roomDao.selectTurnByRoomId(1L)).willReturn(turnDto);

        List<BoardInfosDto> boards = new ArrayList<>();
        BoardInfosDto boardInfosDto = new BoardInfosDto();
        boardInfosDto.setIsFirstMoved(true);
        boardInfosDto.setPiece("Rook");
        boardInfosDto.setPosition("a2");
        boardInfosDto.setTeam("white");
        boards.add(boardInfosDto);
        given(boardDao.selectBoardInfosByRoomId(1L)).willReturn(boards);

        // when
        ChessGameDto result = springChessService.loadPreviousGame();

        // then
        assertThat(objectMapper.writeValueAsString(result)).isEqualTo(objectMapper.writeValueAsString(expected));


    }
}