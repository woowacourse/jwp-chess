package chess.service;

import chess.domain.game.dto.MoveRequest;
import chess.domain.gameRoom.ChessGame;
import chess.domain.piece.unit.Piece;
import chess.domain.position.Position;
import chess.exception.InvalidDeleteFailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChessServiceTest {

    @Autowired
    private ChessService chessService;

    @Test
    @DisplayName("체스 게임방 삭제 (성공)")
    void deleteChessGameRoomSuccess() {
        String gameId = chessService.addChessGame("zero", "1234");
        chessService.deleteGameByIdAndPassword(gameId, "1234");
        List<String> gameIDs = chessService.getGames().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(gameIDs.contains(gameId)).isEqualTo(false);
    }

    @Test
    @DisplayName("체스 게임방 삭제 (실패)")
    void deleteChessGameRoomFail() {
        String gameId = chessService.addChessGame("zero", "1234");
        Assertions.assertThatThrownBy(() -> chessService.deleteGameByIdAndPassword(gameId, "wrongPass"))
                .isInstanceOf(InvalidDeleteFailException.class);
    }

    @Test
    @DisplayName("체스 이동 이후, 방을 나가더라도 진행한 체스보드를 불러올 수 있다")
    void moveChess() {
        String gameId = chessService.addChessGame("zero", "1234");
        chessService.movePiece(gameId, new MoveRequest("B2", "B4", "WHITE"));
        ChessGame chessGame = chessService.getChessGame(gameId);
        Map<Position, Piece> board = chessGame.getChessBoard().getBoard();

        assertAll(
                () -> assertEquals(board.get(Position.of("B2")), null),
                () -> assertNotEquals(board.get(Position.of("B4")), null)
        );
    }
}