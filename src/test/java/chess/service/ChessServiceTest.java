package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.BoardDao;
import chess.dao.FakeBoardDao;
import chess.dao.FakeGameDao;
import chess.dao.GameDao;
import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Symbol;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.PieceDto;
import chess.dto.RoomDto;
import chess.dto.StatusDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessServiceTest {

    private GameDao gameDao = new FakeGameDao();
    private BoardDao boardDao = new FakeBoardDao();
    private int id;

    private ChessService chessService = new ChessService(gameDao, boardDao);

    @BeforeEach
    void setUp() {
        RoomDto roomDto = new RoomDto("title", "password");
        id = chessService.insertGame(roomDto, new ChessBoard(new NormalPiecesGenerator()));
    }

    @AfterEach
    void clear() {
        gameDao.delete(id);
        boardDao.delete(id);
    }

    @Test
    @DisplayName("게임들의 리스트를 반환한다.")
    void findGame() {
        List<GameDto> game = chessService.findGame();
        String password = game.get(id - 1).getPassword();

        assertThat(password).isEqualTo("password");
    }

    @Test
    @DisplayName("BoardDto를 반환한다.")
    void selectBoard() {
        BoardDto boardDto = chessService.selectBoard(id);
        Map<String, PieceDto> board = boardDto.getBoard();
        Piece piece = board.get("a2").toEntity();

        assertThat(piece).isEqualTo(Piece.of(Color.WHITE, Symbol.PAWN));
    }

    @Test
    @DisplayName("승자를 불러올 수 있다")
    void selectWinner() {
        String winner = chessService.selectWinner(id);

        assertThat(winner).isNull();
    }

    @Test
    @DisplayName("보드를 업데이트할 수 있다.")
    void updateBoard() {
        chessService.updateBoard(id, "a2", "a3");

        ChessBoard chessBoard = boardDao.findById(id);
        Piece piece = chessBoard.selectPiece(Position.of("a3"));

        assertThat(piece).isEqualTo(Piece.of(Color.WHITE, Symbol.PAWN));
    }

    @Test
    @DisplayName("게임의 상태 불러올 수 있다.")
    void selectState() {
        String state = chessService.selectState(id);

        assertThat(state).isEqualTo("WhiteRunning");
    }

    @Test
    @DisplayName("게임의 점수를 불러올 수 있다.")
    void selectStatus() {
        StatusDto statusDto = chessService.selectStatus(id);

        assertThat(statusDto.getWhiteScore()).isEqualTo("38.0");
    }

    @Test
    @DisplayName("게임을 종료시킬 수 있다.")
    void endGame() {
        chessService.endGame(id);
        String state = gameDao.findState(id).toString();

        assertThat(state).isEqualTo("Finish");
    }

    @Test
    @DisplayName("종료되지 않은 게임은 삭제할 수 없다.")
    void deleteGameThrownException() {
        assertThatThrownBy(() ->
                chessService.deleteGame(id, "password"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("진행중인 게임은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호가 다르면 게임을 삭제할 수 없다.")
    void deleteGameThrownExceptionWithPassword() {
        chessService.endGame(id);
        assertThatThrownBy(() ->
                chessService.deleteGame(id, "passwo"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("비밀번호가 일치하면 게임을 삭제할 수 있다.")
    void deleteGame() {
        chessService.endGame(id);
        chessService.deleteGame(id, "password");

        assertThat(gameDao.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("게임을 재시작할 수 있다.")
    void restartGame() {
        chessService.endGame(id);
        chessService.restartGame(id);
        String state = gameDao.findState(id).toString();

        assertThat(state).isEqualTo("WhiteRunning");
    }
}
