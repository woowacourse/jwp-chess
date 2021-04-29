package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.position.Position;
import chess.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    GameService gameService;

    @Mock
    GameDao gameDao;

    @Mock
    PieceDao pieceDao;


    private ChessGameManager chessGameManager;
    @BeforeEach
    void setUp() {
        chessGameManager = new ChessGameManager();
        chessGameManager.start();
    }

    @Test
    void 새로운_게임을_저장한다() {
        // given
        SavedGameDto savedGameDto = new SavedGameDto(
                ChessBoardDto.from(chessGameManager.getBoard()),
                chessGameManager.getCurrentTurnColor().name());
        int newGameId = 1;

        // when
        given(gameDao.saveGame(savedGameDto)).willReturn(newGameId);

        // then
        CommonDto<NewGameDto> commonDto = gameService.saveNewGame();
        assertThat(commonDto.getMessage()).isEqualTo("새로운 게임이 생성되었습니다.");
        assertThat(commonDto.getItem().getGameId()).isEqualTo(newGameId);
    }

    @Test
    void 게임을_불러온다() {
        // given
        int gameId = 1;
        HashMap<Position, Piece> board = new HashMap<Position, Piece>() {{
            put(Position.of("a1"), new Rook(WHITE));
            put(Position.of("e1"), new King(WHITE));
            put(Position.of("e8"), (new King(BLACK)));
        }};
        ChessBoard chessBoard = ChessBoard.from(board);
        chessGameManager.load(chessBoard, Color.WHITE);

        // when
        given(gameDao.selectGameTurnByGameId(gameId)).willReturn("WHITE");
        given(pieceDao.selectPieceByGameId(gameId)).willReturn(ChessBoardDto.from(chessBoard));

        // then
        CommonDto<RunningGameDto> commonDto = gameService.loadGame(1);
        assertThat(commonDto.getMessage()).isEqualTo("게임을 불러왔습니다.");
        assertThat(commonDto.getItem().getChessBoard().size()).isEqualTo(3);
    }

    @Test
    void 체스_말의_이동을_요청한다() {
        // given
        int gameId = 1;
        HashMap<Position, Piece> board = new HashMap<Position, Piece>() {{
            put(Position.of("a1"), new Rook(WHITE));
            put(Position.of("e1"), new King(WHITE));
            put(Position.of("e8"), (new King(BLACK)));
        }};
        ChessBoard chessBoard = ChessBoard.from(board);
        chessGameManager.load(chessBoard, Color.WHITE);

        // when
        given(gameDao.selectGameTurnByGameId(gameId)).willReturn("WHITE");
        given(pieceDao.selectPieceByGameId(gameId)).willReturn(ChessBoardDto.from(chessBoard));

        // then
        CommonDto<RunningGameDto> commonDto = gameService.move(gameId, "e1", "d2");
        assertThat(commonDto.getMessage()).isEqualTo("기물을 이동했습니다.");
        assertDoesNotThrow(() -> commonDto.getItem().getChessBoard().get("d2"));
    }

    @Test
    void 체스_말_이동_시_게임이_끝난_경우를_확인해준다() {
        // given
        int gameId = 1;
        HashMap<Position, Piece> board = new HashMap<Position, Piece>() {{
            put(Position.of("a1"), new Rook(WHITE));
            put(Position.of("e1"), new King(WHITE));
            put(Position.of("e8"), (new Rook(BLACK)));
        }};
        ChessBoard chessBoard = ChessBoard.from(board);
        chessGameManager.load(chessBoard, BLACK);

        // when
        given(gameDao.selectGameTurnByGameId(gameId)).willReturn("BLACK");
        given(pieceDao.selectPieceByGameId(gameId)).willReturn(ChessBoardDto.from(chessBoard));
        chessGameManager.end();

        // then
        CommonDto<RunningGameDto> commonDto = gameService.move(gameId, "e8", "e5");
        assertThat(commonDto.getMessage()).isEqualTo("게임이 끝났습니다.");
        assertThat(commonDto.getItem().getChessBoard().size()).isEqualTo(3);
    }
}