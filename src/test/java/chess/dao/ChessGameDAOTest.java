package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.ChessGame;
import chess.dto.GameCreationDTO;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChessGameDAOTest {

    @Autowired
    private ChessGameDAO dao;

    @Test
    @DisplayName("체스 게임방을 생성한다")
    void makeChessGameRoom() {
        long id = dao.addGame(new GameCreationDTO("zero", "1234"));
        assertThat(id).isNotEqualTo(0);
    }

    @Test
    @DisplayName("모든 체스 게임방을 불러온다")
    void findChessGameRoom() {
        // arrange
        dao.addGame(new GameCreationDTO("test1", "123"));
        dao.addGame(new GameCreationDTO("test2", "123"));
        dao.addGame(new GameCreationDTO("test3", "123"));

        // act
        List<ChessGame> games = dao.findAllGames();

        // assert
        assertThat(games.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("체스 게임방을 ID로 불러온다")
    void findChessGameById() {
        // arrange
        String gameName = "test1";
        long id = dao.addGame(new GameCreationDTO(gameName, "123"));

        // act
        ChessGame findGame = dao.findGameById(id);

        // assert
        assertThat(findGame.getName()).isEqualTo(gameName);
    }

    @Test
    @DisplayName("게임을 종료 상태로 업데이트한다")
    void updateGameEnd() {
        // arrange
        long addGameId = dao.addGame(new GameCreationDTO("test1", "123"));

        // act
        long updateGameId = dao.updateGameEnd(addGameId);
        ChessGame updatedGame = dao.findGameById(updateGameId);

        // assert
        assertThat(updatedGame.isEnd()).isTrue();
    }

    @Test
    @DisplayName("체스 게임방을 삭제한다")
    void deleteChessGame() {
        // arrange
        dao.addGame(new GameCreationDTO("zero", "1234"));

        // act
        dao.deleteGame(1);

        // assert
        assertThatThrownBy(() -> dao.findGameById(1))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
