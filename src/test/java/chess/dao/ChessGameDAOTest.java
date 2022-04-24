package chess.dao;

import chess.dto.ChessGameRoomInfoDTO;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChessGameDAOTest {

    @Autowired
    private JdbcTemplate template;

    @Test
    @DisplayName("체스 게임방 생성")
    void makeChessGameRoom() {
        String gameId = addZero();

        Assertions.assertThat(gameId).isNotNull();
    }

    private String addZero() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        String gameId = chessGameDAO.addGame(chessGame);
        return gameId;
    }

    @Test
    @DisplayName("체스 게임방 가져오기")
    void findChessGameRoom() {
        ChessGameDAO chessGameDAO = new ChessGameDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        String gameId = chessGameDAO.addGame(chessGame);
        List<ChessGameRoomInfoDTO> activeGames = chessGameDAO.findActiveGames();

        Assertions.assertThat(activeGames.size()).isNotEqualTo(0);
    }
}
