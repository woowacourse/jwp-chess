package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.Position;
import chess.domain.game.ChessGame;

@SpringBootTest
@Transactional
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("데이터를 저장할 수 있어야 한다.")
    @Test
    void save() {
        final ChessGame chessGame = ChessGame.initializeChessGame();

        final Long gameId = gameRepository.save(chessGame).getId();
        assertThat(gameId).isNotEqualTo(0L);
        gameRepository.remove(gameId);
    }

    @DisplayName("데이터를 조회할 수 있어야 한다.")
    @Test
    void findById() {
        final ChessGame expectedChessGame = ChessGame.initializeChessGame();

        final Long gameId = gameRepository.save(expectedChessGame).getId();
        final ChessGame chessGame = gameRepository.findById(gameId);

        assertAll(() -> {
            assertThat(chessGame.getColorOfCurrentTurn()).isEqualTo(expectedChessGame.getColorOfCurrentTurn());
            assertThat(chessGame.isFinished()).isEqualTo(expectedChessGame.isFinished());
        });
        gameRepository.remove(gameId);
    }

    @DisplayName("데이터를 수정할 수 있어야 한다.")
    @Test
    void update() {
        final ChessGame chessGame = ChessGame.initializeChessGame();

        final ChessGame expectedChessGame = gameRepository.save(chessGame);

        expectedChessGame.movePiece(Position.from("a2"), Position.from("a4"));
        expectedChessGame.end();

        final ChessGame updatedChessGame = gameRepository.update(expectedChessGame);
        assertAll(() -> {
            assertThat(updatedChessGame.getColorOfCurrentTurn()).isEqualTo(expectedChessGame.getColorOfCurrentTurn());
            assertThat(updatedChessGame.isFinished()).isEqualTo(expectedChessGame.isFinished());
        });
        gameRepository.remove(updatedChessGame.getId());
    }
}