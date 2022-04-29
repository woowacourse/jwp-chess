package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import chess.dto.GameResultDto;
import chess.dto.GameRoomDto;
import chess.dto.PiecesDto;
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
    public void findAllIdAndTitle() {
        when(gameDao.findAllIdAndTitle()).thenReturn(List.of(new GameRoomDto(1L, "방 제목")));

        List<GameRoomDto> actual = chessGameService.getAllGames();

        assertThat(actual.get(0).getId()).isEqualTo(1L);
        assertThat(actual.get(0).getTitle()).isEqualTo("방 제목");
    }

    @DisplayName("제목과 비밀번호로 방을 생성하면 방의 아이디를 반환한다.")
    @Test
    public void create() {
        when(gameDao.createByTitleAndPassword("hello", "world")).thenReturn(1L);

        long actual = chessGameService.create("hello", "world");

        assertThat(actual).isEqualTo(1L);
    }
    
    @DisplayName("PiecesDto를 반환한다.")
    @Test
    public void getCurrentGame() {
        when(gameDao.findEndFlagById(2L)).thenReturn(false);
        when(gameDao.findTurnById(2L)).thenReturn(Color.BLACK);
        when(pieceDao.findAllByGameId(2L)).thenReturn(new Pieces(List.of(new Pawn(Color.BLACK, Position.of("a1")))));

        PiecesDto actual = chessGameService.findCurrentPieces(2L);

        assertThat(actual.getPieces()).hasSize(1);
    }

    @DisplayName("PiecesDto를 반환한다.")
    @Test
    public void calculateGameResult() {
        when(gameDao.findEndFlagById(2L)).thenReturn(false);
        when(gameDao.findTurnById(2L)).thenReturn(Color.BLACK);
        when(pieceDao.findAllByGameId(2L)).thenReturn(new Pieces(List.of(new Pawn(Color.BLACK, Position.of("a1")))));

        GameResultDto actual = chessGameService.calculateGameResult(2L);

        assertThat(actual.getBlackScore()).isEqualTo(1);
        assertThat(actual.getWhiteScore()).isEqualTo(0);
        assertThat(actual.getWinner()).isEqualTo("없음");
    }

}
