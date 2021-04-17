package chess.repository;

import static chess.utils.TestFixture.TEST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.ChessGame;
import chess.domain.color.type.TeamColor;
import chess.domain.position.Position;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessGameRepositoryTest {

    @Autowired
    private ChessGameRepository chessGameRepository;

    @DisplayName("저장 및 조회")
    @Test
    void saveAndFind() {
        ChessGame chessGame = new ChessGame(TEST_TITLE);

        Long gameId = chessGameRepository.save(chessGame);
        ChessGame foundChessGame = chessGameRepository.findById(gameId);

        assertThat(foundChessGame.getId()).isEqualTo(gameId);
    }

    @DisplayName("저장되어있는 모든 체스 게임 조회")
    @Test
    void findAll() {
        String title1 = "title1";
        String title2 = "title2";
        Long gameId1 = chessGameRepository.save(new ChessGame(title1));
        Long gameId2 = chessGameRepository.save(new ChessGame(title2));

        List<ChessGame> foundAllChessGames = chessGameRepository.findAll();

        assertThat(foundAllChessGames).hasSize(2);

        List<String> allTitles = foundAllChessGames.stream()
            .map(ChessGame::getTitle)
            .collect(Collectors.toList());

        List<Long> allIds = foundAllChessGames.stream()
            .map(ChessGame::getId)
            .collect(Collectors.toList());

        assertThat(allTitles).contains(title1, title2);
        assertThat(allIds).contains(gameId1, gameId2);
    }

    @DisplayName("체스 게임 업데이트")
    @Test
    void update() {
        ChessGame chessGame = new ChessGame(TEST_TITLE);
        Long savedGameId = chessGameRepository.save(chessGame);

        ChessGame savedChessGame = chessGameRepository.findById(savedGameId);
        assertThat(savedChessGame.getCurrentTurnTeamColor()).isSameAs(TeamColor.WHITE);

        savedChessGame.movePiece(Position.of("b2"), Position.of("b4"));
        chessGameRepository.update(savedChessGame);

        ChessGame updatedChessGame = chessGameRepository.findById(savedGameId);

        assertThat(updatedChessGame.getCurrentTurnTeamColor()).isSameAs(TeamColor.BLACK);
        assertThat(updatedChessGame.getBoardStatus()).isEqualTo(""
            + "RNBQKBNR"
            + "PPPPPPPP"
            + "........"
            + "........"
            + ".p......"
            + "........"
            + "p.pppppp"
            + "rnbqkbnr"
        );
    }

    @DisplayName("체스 게임 삭제")
    @Test
    void delete() {
        ChessGame chessGame = new ChessGame(TEST_TITLE);
        Long savedGameId = chessGameRepository.save(chessGame);
        assertThat(chessGameRepository.findAll()).hasSize(1);

        chessGameRepository.deleteById(savedGameId);

        assertThat(chessGameRepository.findAll()).isEmpty();
    }
}