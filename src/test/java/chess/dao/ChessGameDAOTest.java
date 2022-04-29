package chess.dao;

import chess.domain.gameRoom.ChessGame;
import chess.domain.gameRoom.dao.ChessGameRoomDAO;
import chess.domain.gameRoom.dto.ChessGameRoomShowInfoDTO;
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
        ChessGameRoomDAO chessGameDAO = new ChessGameRoomDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        chessGame.setPassword("1234");
        String gameId = chessGameDAO.addGame(chessGame);
        return gameId;
    }

    @Test
    @DisplayName("체스 게임방 가져오기")
    void findChessGameRoom() {
        ChessGameRoomDAO chessGameDAO = new ChessGameRoomDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        chessGame.setPassword("1234");
        String gameId = chessGameDAO.addGame(chessGame);
        List<ChessGameRoomShowInfoDTO> activeGames = chessGameDAO.findActiveGames();

        Assertions.assertThat(activeGames.size()).isNotEqualTo(0);
    }
    @Test
    @DisplayName("체스 게임방 삭제 (성공)")
    void deleteChessGameRoom() {
        ChessGameRoomDAO chessGameDAO = new ChessGameRoomDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        chessGame.setPassword("1234");
        String gameId = chessGameDAO.addGame(chessGame);

        chessGameDAO.deleteGameByIdAndPassword(gameId, "1234");

        List<ChessGameRoomShowInfoDTO> activeGames = chessGameDAO.findActiveGames();
        Assertions.assertThat(activeGames.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("체스 게임방 삭제 (실패)")
    void deleteChessGameRoomFail() {
        ChessGameRoomDAO chessGameDAO = new ChessGameRoomDAO(template);
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        chessGame.setPassword("1234");
        String gameId = chessGameDAO.addGame(chessGame);

        chessGameDAO.deleteGameByIdAndPassword(gameId, "12345");

        List<ChessGameRoomShowInfoDTO> activeGames = chessGameDAO.findActiveGames();
        Assertions.assertThat(activeGames.size()).isNotEqualTo(0);
    }
}
