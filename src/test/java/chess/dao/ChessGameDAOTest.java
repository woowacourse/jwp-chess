package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dto.ChessGameRoomInfoDTO;
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

    @Test
    @DisplayName("체스 게임방 생성")
    void makeChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        long id = chessGameDAO.addGame(new GameCreationDTO("zero", "1234"));

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("체스 게임방 가져오기")
    void findChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        List<ChessGameRoomInfoDTO> activeGames = chessGameDAO.findActiveGames();

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
