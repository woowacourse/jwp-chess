package chess.dao;

import chess.dto.ChessGameRoomInfoDTO;
import chess.dto.GameCreationDTO;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        Assertions.assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("체스 게임방 가져오기")
    void findChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        List<ChessGameRoomInfoDTO> activeGames = chessGameDAO.findActiveGames();

        Assertions.assertThat(activeGames.size()).isNotEqualTo(0);
    }
}
