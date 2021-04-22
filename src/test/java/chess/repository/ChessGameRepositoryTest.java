package chess.repository;

import static chess.utils.TestFixture.TEST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.color.type.TeamColor;
import chess.domain.game.ChessGame;
import chess.domain.password.PasswordEncoder;
import chess.web.exception.GameNotExistsException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessGameRepositoryTest {

    @Autowired
    private ChessGameRepository chessGameRepository;

    @AfterEach
    void tearDown() {
        chessGameRepository.deleteAll();
    }

    @DisplayName("findById() 결과가 없을 때 테스트")
    @Test
    void findByIdResultNone() {
        Optional<ChessGame> foundOptionalChessGame = chessGameRepository.findById(1L);

        assertThat(foundOptionalChessGame.isPresent()).isFalse();
    }

    @DisplayName("저장 및 조회")
    @Test
    void saveAndFind() {
        ChessGame chessGame = new ChessGame(TEST_TITLE);

        Long gameId = chessGameRepository.save(chessGame);
        ChessGame foundChessGame = chessGameRepository.findById(gameId)
            .orElseThrow(GameNotExistsException::new);

        assertThat(foundChessGame.getId()).isEqualTo(gameId);
    }

    @DisplayName("흑 플레이어의 비밀번호가 null인 모든 체스게임 조회")
    @Test
    void findAllBlackPlayerPasswordIsNull() {
        chessGameRepository.save(new ChessGame("test1", "encryptedWhitePassword1", null));
        chessGameRepository.save(new ChessGame("test2", "encryptedWhitePassword2", null));
        chessGameRepository.save(new ChessGame("test3", "encryptedWhitePassword3", "encryptedBlackPlayerPassword1"));
        chessGameRepository.save(new ChessGame("test4", "encryptedWhitePassword4", "encryptedBlackPlayerPassword2"));
        chessGameRepository.save(new ChessGame("test5", "encryptedWhitePassword5", "encryptedBlackPlayerPassword3"));

        List<ChessGame> allBlackPlayerPasswordIsNull = chessGameRepository.findAllBlackPlayerPasswordIsNull();

        assertThat(allBlackPlayerPasswordIsNull).hasSize(2);
        assertThat(allBlackPlayerPasswordIsNull)
            .extracting(ChessGame::getTitle)
            .containsExactlyInAnyOrder("test1", "test2");
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

    @DisplayName("체스 게임 상태 업데이트")
    @Test
    void update() {
        ChessGame chessGame = new ChessGame(TEST_TITLE);
        Long savedGameId = chessGameRepository.save(chessGame);

        ChessGame savedChessGame = chessGameRepository.findById(savedGameId)
            .orElseThrow(GameNotExistsException::new);
        assertThat(savedChessGame.getCurrentTurnTeamColor()).isSameAs(TeamColor.WHITE);
        assertThat(savedChessGame.getEncryptedBlackPlayerPassword()).isNull();

        savedChessGame.movePiece("b2", "b4");
        String rawBlackPlayerPassword = "rawBlackPlayerPassword";
        String encryptedBlackPlayerPassword = PasswordEncoder.encrypt(rawBlackPlayerPassword);
        savedChessGame.joinBlackPlayerWithPassword(rawBlackPlayerPassword);
        chessGameRepository.update(savedChessGame);

        ChessGame updatedChessGame = chessGameRepository.findById(savedGameId)
            .orElseThrow(GameNotExistsException::new);

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
        assertThat(updatedChessGame.getEncryptedBlackPlayerPassword()).isEqualTo(encryptedBlackPlayerPassword);
    }

    @DisplayName("체스 게임 삭제")
    @Test
    void delete() {
        ChessGame chessGame = new ChessGame(TEST_TITLE);
        Long savedGameId = chessGameRepository.save(chessGame);
        assertThat(chessGameRepository.findById(savedGameId)).isNotNull();

        chessGameRepository.deleteById(savedGameId);

        assertThat(chessGameRepository.findById(savedGameId)).isNull();
    }
}