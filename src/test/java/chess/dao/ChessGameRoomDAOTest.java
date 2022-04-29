package chess.dao;

import chess.domain.gameRoom.ChessGame;
import chess.domain.gameRoom.dao.ChessGameRoomDAO;
import chess.domain.gameRoom.dto.ChessGameRoomPassInfoDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ChessGameRoomDAOTest {

    @Autowired
    private ChessGameRoomDAO chessGameRoomDAO;

    @Test
    @DisplayName("체스 게임방 가져오기")
    void findChessGameRoom() {
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zero");
        chessGame.setPassword("1234");
        String id = chessGameRoomDAO.addGame(chessGame);

        ChessGameRoomPassInfoDTO chessGameRoom = chessGameRoomDAO.findPassGameById(id);

        assertAll(
                () -> assertEquals(chessGameRoom.getName(), "zero"),
                () -> assertEquals(chessGameRoom.getPassword(), "1234")
        );
    }

    @Test
    @DisplayName("체스 게임방 생성")
    void makeChessGameRoom() {
        String gameId = addZeroRoom();

        Assertions.assertThat(gameId).isNotNull();
    }

    private String addZeroRoom() {
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName("zeroRoom");
        chessGame.setPassword("room1234");
        String gameId = chessGameRoomDAO.addGame(chessGame);
        return gameId;
    }
}
