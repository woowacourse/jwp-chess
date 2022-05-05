package chess.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.ChessGame;
import chess.domain.Command;
import chess.dto.ChessGameDto;

@SpringBootTest
@Transactional
class ChessGameDaoTest {
    private ChessGameDao chessGameDao;
    private PieceDao pieceDao;
    private int savedId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        String gameName = "test_game1";
        savedId = chessGameDao.save(new ChessGame(gameName, "1234"));
        savePieces(savedId);

        chessGameDao.save(new ChessGame("test_game2", "1234"));
        chessGameDao.save(new ChessGame("test_game3", "1234"));
    }

    private void savePieces(int gameId) {
        ChessGame chessGame = new ChessGame();
        chessGame.progress(Command.from("start"));
        pieceDao.save(chessGame, gameId);
    }

    @Test
    @DisplayName("게임을 DB에 저장해야 합니다.")
    void save() {
        String gameName = "test_game4";
        int savedId = chessGameDao.save(new ChessGame(gameName, "1234"));
        savePieces(savedId);
        String savedName = chessGameDao.findById(savedId).getGameName();
        assertThat(savedName).isEqualTo(gameName);
    }

    @Test
    @DisplayName("저장된 게임을 id로 조회해올 수 있어야 합니다.")
    void findById() {
        ChessGame chessGame = chessGameDao.findById(savedId);
        assertThat(chessGame.getGameName()).isEqualTo("test_game1");
    }

    @Test
    @DisplayName("저장된 게임들을 조회해와야 합니다.")
    void findAllChessGames() {
        List<ChessGameDto> chessGames = chessGameDao.findAllChessGames();
        List<String> chessGameNames = chessGames.stream()
            .map(ChessGameDto::getGameName)
            .collect(Collectors.toList());

        assertThat(chessGameNames).hasSize(3)
            .contains("test_game1", "test_game2", "test_game3");
    }

    @Test
    @DisplayName("저장된 게임의 상태를 변경할 수 있어야 합니다.")
    void update() {
        String newTurn = "black";
        chessGameDao.update(newTurn, savedId);
        ChessGame chessGame = chessGameDao.findById(savedId);
        String currentTurn = chessGame.getState().getTurn();

        assertThat(currentTurn).isEqualTo(newTurn);
    }

    @Test
    @DisplayName("저장된 게임을 삭제할 수 있어야 합니다.")
    void delete() {
        chessGameDao.delete(savedId);
        List<ChessGameDto> chessGames = chessGameDao.findAllChessGames();
        List<String> chessGameNames = chessGames.stream()
            .map(ChessGameDto::getGameName)
            .collect(Collectors.toList());

        assertThat(chessGameNames).hasSize(2)
            .contains("test_game2", "test_game3");
    }
}