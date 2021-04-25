package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.webdto.ChessGameTableDto;
import chess.webdto.GameRoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class SpringChessGameDaoTest {

    @Autowired
    SpringChessGameDao springChessGameDao;

    private int testRoomNumber;

    @BeforeEach
    void setUp() {
        testRoomNumber = springChessGameDao.createGameRoom("test1");
    }

    @Test
    void loadGameRooms() {
        final List<GameRoomDto> gameRoomDtos = springChessGameDao.loadGameRooms();
        final Optional<GameRoomDto> test1 = gameRoomDtos.stream()
                .filter(gameRoomDto -> gameRoomDto.getRoomId() == testRoomNumber)
                .filter(gameRoomDto -> gameRoomDto.getRoomName().equals("test1"))
                .findFirst();
        assertThat(test1.isPresent()).isTrue();
    }

    @Test
    void ChessGameInfoTest() {
        springChessGameDao.createChessGameInfo(testRoomNumber, "black", true);
        ChessGameTableDto chessGameTableDto = springChessGameDao.readChessGameInfo(testRoomNumber);
        assertThat(chessGameTableDto.getCurrentTurnTeam()).isEqualTo("black");
        assertThat(chessGameTableDto.getIsPlaying()).isTrue();

        springChessGameDao.updateChessGameInfo(testRoomNumber, "white", false);
        chessGameTableDto = springChessGameDao.readChessGameInfo(testRoomNumber);
        assertThat(chessGameTableDto.getCurrentTurnTeam()).isEqualTo("white");
        assertThat(chessGameTableDto.getIsPlaying()).isFalse();
    }

    @Test
    void TeamInfoTest() {
        springChessGameDao.createTeamInfo(testRoomNumber, "black", "testPieceInfo");
        String pieceInfo = springChessGameDao.readTeamInfo(testRoomNumber, "black");
        assertThat(pieceInfo).isEqualTo("testPieceInfo");

        springChessGameDao.updateTeamInfo(testRoomNumber, "black", "updatePieceInfo");
        pieceInfo = springChessGameDao.readTeamInfo(testRoomNumber, "black");
        assertThat(pieceInfo).isEqualTo("updatePieceInfo");
    }

    @Test
    void deleteChessGameTest() {
        springChessGameDao.deleteChessGame(testRoomNumber);
        assertThatCode(() -> springChessGameDao.readChessGameInfo(testRoomNumber))
                .isInstanceOf(Exception.class);
    }
}
