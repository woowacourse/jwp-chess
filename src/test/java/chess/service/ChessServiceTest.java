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
import chess.domain.piece.generator.PiecesGenerator;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.PieceDto;
import chess.dto.RoomDto;
import chess.dto.StatusDto;
import java.util.HashMap;
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
        String password = game.get(id - 1).getTitle();

        assertThat(password).isEqualTo("title");
    }

    @Test
    @DisplayName("BoardDto를 반환한다.")
    void selectBoard() {
        BoardDto boardDto = chessService.selectBoard(id);
        Map<String, PieceDto> board = boardDto.getBoard();
        Piece piece = createPiece(board.get("a2"));

        assertThat(piece).isEqualTo(Piece.of(Color.WHITE, Symbol.PAWN));
    }

    private Piece createPiece(PieceDto pieceDto) {
        return Piece.of(Color.valueOf(pieceDto.getColor()), Symbol.valueOf(pieceDto.getSymbol()));
    }

    @Test
    @DisplayName("승자를 불러올 수 있다")
    void selectWinner() {
        RoomDto roomDto = new RoomDto("title", "password");
        id = chessService.insertGame(roomDto, createTestBoard());
        String winner = chessService.selectWinner(id);

        assertThat(winner).isEqualTo("WHITE");
    }

    private ChessBoard createTestBoard() {
        final Map<Position, Piece> testPieces = new HashMap<>(Map.ofEntries(
                Map.entry(Position.of("a1"), Piece.of(Color.WHITE, Symbol.KING)),
                Map.entry(Position.of("b3"), Piece.of(Color.WHITE, Symbol.PAWN)),
                Map.entry(Position.of("c4"), Piece.of(Color.WHITE, Symbol.PAWN)),
                Map.entry(Position.of("a7"), Piece.of(Color.BLACK, Symbol.PAWN)),
                Map.entry(Position.of("c5"), Piece.of(Color.BLACK, Symbol.PAWN))
        ));
        PiecesGenerator.fillEmptyPiece(testPieces);
        return new ChessBoard(() -> testPieces);
    }

    @Test
    @DisplayName("게임의 상태 불러올 수 있다.")
    void selectState() {
        String state = chessService.selectState(id);

        assertThat(state).isEqualTo("WhiteTurn");
    }

    @Test
    @DisplayName("게임의 점수를 불러올 수 있다.")
    void selectStatus() {
        StatusDto statusDto = chessService.selectStatus(id);

        assertThat(statusDto.getWhiteScore()).isEqualTo("38.0");
    }

    @Test
    @DisplayName("보드에서 말을 움직일 수 있다.")
    void movePiece() {
        chessService.movePiece(id, "a2", "a3");
        BoardDto boardDto = BoardDto.of(boardDao.findById(id));
        ChessBoard chessBoard = boardDto.toBoard();
        Piece piece = chessBoard.selectPiece(Position.of("a3"));

        assertThat(piece).isEqualTo(Piece.of(Color.WHITE, Symbol.PAWN));
    }

    @Test
    @DisplayName("게임을 종료시킬 수 있다.")
    void endGame() {
        chessService.endGame(id);
        String state = gameDao.findState(id).getValue();

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
        String state = gameDao.findState(id).getValue();

        assertThat(state).isEqualTo("WhiteTurn");
    }
}
