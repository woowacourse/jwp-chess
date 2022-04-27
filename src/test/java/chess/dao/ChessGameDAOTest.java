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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChessGameDAOTest {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ChessGameDAO dao;

    @Test
    @DisplayName("체스 게임방을 생성한다")
    void makeChessGameRoom() {
        long id = dao.addGame(new GameCreationDTO("zero", "1234"));
        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 체스 게임방을 불러온다")
    void findChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        List<ChessGame> activeGames = chessGameDAO.findAllGames();
        assertThat(activeGames.size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("체스 게임방을 삭제한다")
    void deleteChessGame() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        chessGameDAO.addGame(new GameCreationDTO("zero", "1234"));

        chessGameDAO.deleteGame("1");

        assertThatThrownBy(() -> chessGameDAO.findGameById("1"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
