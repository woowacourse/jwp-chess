package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import chess.domain.room.Room;
import chess.dto.GameResultDto;
import chess.dto.MoveCommandDto;
import chess.dto.PiecesDto;
import chess.dto.RoomDto;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

//junit 5 미만이라면 @RunWith
//junit 5 이상이라면 @ExtendsWith
@RunWith(MockitoJUnitRunner.class)
public class ChessGameServiceTest {

    @InjectMocks
    public ChessGameService chessGameService;

    @Mock
    private PieceDao pieceDao;

    @Mock
    private GameDao gameDao;

    @Before
    public void init() {
        chessGameService = new ChessGameService(pieceDao, gameDao);
    }

    @DisplayName("생성되어있는 게임들의 정보를 모두 가져온다.")
    @Test
    public void findAllRoom() {
        when(gameDao.findAllRoom()).thenReturn(List.of(new Room(1L, false, Color.WHITE, "방 제목", "비밀번호")));

        List<RoomDto> actual = chessGameService.getAllGames();

        assertThat(actual.get(0).getId()).isEqualTo(1L);
        assertThat(actual.get(0).getTitle()).isEqualTo("방 제목");
    }

    @DisplayName("제목과 비밀번호로 방을 생성하면 방의 아이디를 반환한다.")
    @Test
    public void create() {
        Room room = new Room("hello", "world");
        when(gameDao.createByTitleAndPassword(room)).thenReturn(1L);

        long actual = chessGameService.create(room);

        assertThat(actual).isEqualTo(1L);
    }

    @DisplayName("게임 아이디에 맞는 PiecesDto를 반환한다.")
    @Test
    public void findCurrentPieces() {
        when(gameDao.findRoomById(2L)).thenReturn(new Room(2L, false, Color.WHITE, "방 제목", "방 비밀번호"));
        when(pieceDao.findAllByGameId(2L)).thenReturn(new Pieces(List.of(new Pawn(Color.WHITE, Position.of("a1")))));

        PiecesDto actual = chessGameService.findCurrentPieces(2L);

        assertThat(actual.getPieces()).hasSize(1);
    }

    @DisplayName("게임 점수와 승자를 반환한다.")
    @Test
    public void calculateGameResult() {
        when(gameDao.findRoomById(2L)).thenReturn(new Room(2L, false, Color.WHITE, "방 제목", "방 비밀번호"));
        when(pieceDao.findAllByGameId(2L)).thenReturn(new Pieces(List.of(new Pawn(Color.WHITE, Position.of("a1")))));

        GameResultDto actual = chessGameService.calculateGameResult(2L);

        assertThat(actual.getBlackScore()).isEqualTo(0);
        assertThat(actual.getWhiteScore()).isEqualTo(1);
        assertThat(actual.getWinner()).isEqualTo("없음");
    }

    @DisplayName("source 포지션에 있는 기물을 target 포지션으로 이동한다.")
    @Test
    public void move() {
        //given
        MoveCommandDto moveCommandDto = new MoveCommandDto("a2", "a3");

        when(gameDao.findRoomById(2L)).thenReturn(new Room(2L, false, Color.WHITE, "제목", "비밀번호"));
        when(pieceDao.findAllByGameId(2L)).thenReturn(ChessmenInitializer.init());
        when(pieceDao.exists(2L, moveCommandDto.getTarget())).thenReturn(false);

        //when
        chessGameService.move(2L, moveCommandDto);

        //then
        PiecesDto currentPieces = chessGameService.findCurrentPieces(2L);

        boolean targetPositionEmptyFlag = currentPieces.getPieces()
            .stream()
            .anyMatch(x -> x.getPosition().equals("a3"));
        boolean sourcePositionEmptyFlag = currentPieces.getPieces()
            .stream()
            .anyMatch(x -> x.getPosition().equals("a2"));

        assertThat(targetPositionEmptyFlag).isTrue();
        assertThat(sourcePositionEmptyFlag).isFalse();
    }

    @DisplayName("id와 password로 게임을 삭제한다.")
    @Test
    public void cleanGameByIdAndPassword() {
        when(gameDao.findRoomById(2L)).thenReturn(new Room(2L, true, Color.WHITE, "제목", "비밀번호"));

        assertThatCode(() -> chessGameService.cleanGameByIdAndPassword(2L, "비밀번호"))
            .doesNotThrowAnyException();
    }

    @DisplayName("password가 다르면 게임을 삭제할 수 없다.")
    @Test
    public void cleanGameByIdAndPassword_fail_with_invalid_password() {
        when(gameDao.findRoomById(2L)).thenReturn(new Room(2L, true, Color.WHITE, "제목", "비밀번호"));

        assertThatCode(() -> chessGameService.cleanGameByIdAndPassword(2L, "가짜비밀번호"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("잘못된 방 비밀번호 입니다. 다시 입력해주세요.");
    }

    @DisplayName("게임이 종료되지 않으면 게임을 삭제할 수 없다.")
    @Test
    public void cleanGameByIdAndPassword_fail_with_false_end_flag() {
        when(gameDao.findRoomById(2L)).thenReturn(new Room(2L, false, Color.WHITE, "제목", "비밀번호"));

        assertThatCode(() -> chessGameService.cleanGameByIdAndPassword(2L, "비밀번호"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("아직 게임이 끝나지 않아 삭제할 수 없습니다.");
    }

}
