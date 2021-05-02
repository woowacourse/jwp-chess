package chess.repository;

import chess.domain.board.ChessBoard;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChessRepositoryTest {

    @Autowired
    ChessRepository chessRepository;

    private Long id;

    @BeforeEach
    void setUp() {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test");
        id = chessRepository.addGame(chessGame, chessGame.getTitle());
    }

    @Test
    void findGame() {
        assertThat(chessRepository.findGame("test")).isNotEmpty();
    }

    @Test
    void addGame() {
        ChessGame addingChessGame = new ChessGame(2L, Color.WHITE, false, new ChessBoard(), "add-test");
        chessRepository.addGame(addingChessGame, addingChessGame.getTitle());

        assertThat(chessRepository.findGame("add-test")).isNotEmpty();
    }

    @Test
    void loadGame() {
        ChessGame loadingChessGame = new ChessGame(3L, Color.WHITE, false, new ChessBoard(), "load-test");
        Long id = chessRepository.addGame(loadingChessGame, loadingChessGame.getTitle());

        assertThat(chessRepository.loadGame(id).getTitle()).isEqualTo("load-test");
    }

    @Test
    void saveGame() {
        ChessGame savingChessGame = new ChessGame(3L, Color.WHITE, false, new ChessBoard(), "save-test");
        Long id = chessRepository.addGame(savingChessGame, savingChessGame.getTitle());
        ChessGame savingChessGameChanged = new ChessGame(3L, Color.BLACK, true, new ChessBoard(), "save-test" +
                "-expected");
        chessRepository.saveGame(id, savingChessGameChanged);

        assertThat(chessRepository.loadGame(id).getTurn()).isEqualTo(Color.BLACK.toString());
    }

    @Test
    void terminateGame() {
        chessRepository.terminateGame(id);
        ChessGame terminatedGame = chessRepository.loadGame(id);

        assertThat(terminatedGame.isFinished()).isEqualTo(true);
    }

    @Test
    void restart() {
        ChessGame chessGame = new ChessGame(1L, Color.BLACK, false, new ChessBoard(), "test");
        chessRepository.restart(id, chessGame);
        ChessGame loaded = chessRepository.loadGame(id);

        assertThat(loaded.getTurn()).isEqualTo(Color.BLACK.toString());
    }

    @Test
    void findAllGames() {
        int size = chessRepository.findAllGames().size();
        ChessGame adding = new ChessGame(3L, Color.WHITE, false, new ChessBoard(), "save-test");
        chessRepository.addGame(adding, adding.getTitle());

        assertThat(chessRepository.findAllGames().size() - 1).isEqualTo(size);
    }
}