package chess.dao;

import chess.dto.ChessGameRoomInfoDTO;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ChessGameDAOTest {

    @Autowired
    private JdbcTemplate template;

    @Test
    @DisplayName("체스 게임방 생성")
    void makeChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        String gameId = chessGameDAO.addGame(chessGame);

        Assertions.assertThat(gameId).isNotNull();
    }

    @Test
    @DisplayName("체스 게임방 가져오기")
    void findChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        List<ChessGameRoomInfoDTO> activeGames = chessGameDAO.findActiveGames();

        Assertions.assertThat(activeGames.size()).isNotEqualTo(0);
    }
}
