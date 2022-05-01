package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.command.MoveCommand;
import chess.domain.game.GameResult;
import chess.domain.game.LogIn;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessGameServiceTest {
    private static final String GAME_ID = "1234";
    private ChessGameService chessGameService;
    private final LogIn LOG_IN_DTO = new LogIn(GAME_ID, GAME_ID);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        PieceDao pieceDao = new PieceDao(jdbcTemplate);
        GameDao gameDao = new GameDao(jdbcTemplate);
        chessGameService = new ChessGameService(pieceDao, gameDao);
        chessGameService.createGame(LOG_IN_DTO);
    }

    @DisplayName("validateLogIn로 로그인 시 비밀번호가 맞는지 검증한다.")
    @Test
    void validateLogIn() {
        chessGameService.validateLogIn(
                new LogIn(GAME_ID, GAME_ID));
    }

    @DisplayName("getRooms로 현재 생성된 방들을 불러온다.")
    @Test
    void getRooms() {
        assertThat(chessGameService.getRooms()).hasSize(1);
    }

    @DisplayName("getPieces 로 아이디에 맞는 게임 말들을 불러온다.")
    @Test
    void getPieces() {
        assertThat(chessGameService.getPieces(GAME_ID).getPieces())
                .hasSize(32);
    }

    @DisplayName("calculateGameResult 아이디에 맞는 게임 결과를 불러온다.")
    @Test
    void calculateGameResult() {
        GameResult resultDto = chessGameService.calculateGameResult(GAME_ID);
        assertThat(resultDto.getBlackScore()).isEqualTo(38);
        assertThat(resultDto.getWhiteScore()).isEqualTo(38);
        assertThat(resultDto.getWinner().getName()).isEqualTo("없음");
    }

    @DisplayName("cleanGame로 아이디에 맞는 방을 지운다.")
    @Test
    void cleanGame() {
        chessGameService.deleteGame(LOG_IN_DTO);
        assertThatThrownBy(() -> chessGameService.getGameStatus(GAME_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 제목의 방을 찾을 수 없습니다.");
    }

    @DisplayName("move로 아이디의 말 위치를 움직인다.")
    @Test
    void move() {
        chessGameService.move(GAME_ID, new MoveCommand("a2", "a4"));
        Pieces pieces = chessGameService.getPieces(GAME_ID);
        assertThat(pieces.extractPiece(Position.of("a4")).isPawn())
                .isTrue();
    }

    @DisplayName("changeToEnd로 아이디에 맞는 방을 게임종료로 설정한다.")
    @Test
    void changeToEnd() {
        assertThat(chessGameService.getGameStatus(GAME_ID).getEnd())
                .isFalse();
        chessGameService.changeToEnd(GAME_ID);
        assertThat(chessGameService.getGameStatus(GAME_ID).getEnd())
                .isTrue();
    }

    @DisplayName("validateEnd로 해당 아이디의 방 게임종료 되었음을 검증한다.")
    @Test
    void validateEnd() {
        assertThatThrownBy(() -> chessGameService.validateEnd(GAME_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진행중인 체스방은 삭제할 수 없습니다.");
        chessGameService.changeToEnd(GAME_ID);
        chessGameService.validateEnd(GAME_ID);
    }
}
