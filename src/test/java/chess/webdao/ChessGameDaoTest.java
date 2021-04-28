package chess.webdao;

import chess.webdto.ChessGameTableDto;
import chess.webdto.GameRoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class ChessGameDaoTest {

    @Autowired
    ChessGameDao chessGameDao;

    private int testRoomNumber;

    @BeforeEach
    void setUp() {
        testRoomNumber = chessGameDao.createGameRoom("test1");
    }

    @Test
    void loadGameRooms() {
        final List<GameRoomDto> gameRoomDtos = chessGameDao.loadGameRooms();
        final Optional<GameRoomDto> test1 = gameRoomDtos.stream()
                .filter(gameRoomDto -> gameRoomDto.getRoomId() == testRoomNumber)
                .filter(gameRoomDto -> gameRoomDto.getRoomName().equals("test1"))
                .findFirst();
        assertThat(test1.isPresent()).isTrue();
    }

    @Test
    void ChessGameInfoTest() {
        chessGameDao.createChessGameInfo(testRoomNumber, "black", true);
        ChessGameTableDto chessGameTableDto = chessGameDao.readChessGameInfo(testRoomNumber);
        assertThat(chessGameTableDto.getCurrentTurnTeam()).isEqualTo("black");
        assertThat(chessGameTableDto.getIsPlaying()).isTrue();

        chessGameDao.updateChessGameInfo(testRoomNumber, "white", false);
        chessGameTableDto = chessGameDao.readChessGameInfo(testRoomNumber);
        assertThat(chessGameTableDto.getCurrentTurnTeam()).isEqualTo("white");
        assertThat(chessGameTableDto.getIsPlaying()).isFalse();
    }

    @Test
    void TeamInfoTest() {
        chessGameDao.createTeamInfo(testRoomNumber, "black", "testPieceInfo");
        String pieceInfo = chessGameDao.readTeamInfo(testRoomNumber, "black");
        assertThat(pieceInfo).isEqualTo("testPieceInfo");

        chessGameDao.updateTeamInfo(testRoomNumber, "black", "updatePieceInfo");
        pieceInfo = chessGameDao.readTeamInfo(testRoomNumber, "black");
        assertThat(pieceInfo).isEqualTo("updatePieceInfo");
    }

    @Test
    void deleteChessGameTest() {
        chessGameDao.deleteChessGame(testRoomNumber);
        assertThatCode(() -> chessGameDao.readChessGameInfo(testRoomNumber))
                .isInstanceOf(Exception.class);
    }
}
