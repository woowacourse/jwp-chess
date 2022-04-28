package chess.dao;

import chess.domain.player.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChessGameDaoTest {

    @Autowired
    private RoomDao chessGameDao;

    private final String gameName = "rex_game";
    private final int gameId = 1;

    @Test
    @DisplayName("게임의 아이디를 올바르게 찾아온다.")
    void findChessGameIdByName() {
        int id = chessGameDao.findRoomIdByName(gameName).get();

        assertThat(id).isEqualTo(gameId);
    }

    @Test
    @DisplayName("게임을 올바르게 저장한다.")
    void saveChessGame() {
        String newGameName = "new Game";
        Team turn = Team.WHITE;

        chessGameDao.save(newGameName, turn);
        Optional<Integer> id = chessGameDao.findRoomIdByName(newGameName);

        assertThat(id.isPresent()).isTrue();
    }

    @Test
    @DisplayName("게임의 아이디를 통해 현재 턴을 찾아온다.")
    void findCurrentTurn() {
        String currentTurn = chessGameDao.findTurn(gameId);
        String expected = Team.WHITE.getName();

        assertThat(currentTurn).isEqualTo(expected);
    }

    @Test
    @DisplayName("턴 정보를 업데이트 한다.")
    void updateGameTurn() {
        Team nextTurn = Team.BLACK;
        chessGameDao.updateTurn(gameId, nextTurn);

        String currentTurn = chessGameDao.findTurn(gameId);

        assertThat(currentTurn).isEqualTo(nextTurn.getName());
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    void deleteChessGame() {
        chessGameDao.delete(gameId);

        boolean result = chessGameDao.findRoomIdByName(gameName).isPresent();

        assertThat(result).isFalse();
    }
}
