package chess.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
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
        savedId = chessGameDao.save(gameName, "1234");
        savePieces(gameName, savedId);

        chessGameDao.save("test_game2", "1234");
        chessGameDao.save("test_game3", "1234");
    }

    private void savePieces(String gameName, int gameId) {
        ChessGame chessGame = new ChessGame(gameName);
        chessGame.progress(Command.from("start"));
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);
        pieceDao.save(chessGameDto, gameId);
    }

    @Test
    void save() {
        String gameName = "test_game4";
        int savedId = chessGameDao.save(gameName, "1234");
        savePieces(gameName, savedId);
        String savedName = chessGameDao.findById(savedId).getGameName();
        assertThat(savedName).isEqualTo(gameName);
    }

    @Test
    void findAllChessGames() {
        List<ChessGameDto> chessGames = chessGameDao.findAllChessGames();
        List<String> chessGameNames = chessGames.stream()
            .map(ChessGameDto::getGameName)
            .collect(Collectors.toList());

        assertThat(chessGameNames).hasSize(3)
            .contains("test_game1", "test_game2", "test_game3");
    }

    @Test
    void update() {
        String newTurn = "black";
        chessGameDao.update(newTurn, savedId);
        ChessGame chessGame = chessGameDao.findById(savedId);
        String currentTurn = chessGame.getState().getTurn();

        assertThat(currentTurn).isEqualTo(newTurn);
    }

    @Test
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