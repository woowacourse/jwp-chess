package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Color;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.dto.PieceAndPositionDto;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@JdbcTest
public class ChessDaoTest {
    private static final String DEFAULT_POSITION = "A1";
    private static final String TEST_GAME_PASSWORD = "123";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ChessDao chessDao;
    private int gameId;

    @BeforeEach
    void setUp() {
        chessDao = new ChessDao(jdbcTemplate);
        insertGame();
    }

    private void insertGame() {
        var sql = "INSERT INTO game (game_title, game_password) VALUES(?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement prepareStatement = connection.prepareStatement(sql, new String[]{"game_id"});
            prepareStatement.setString(1, "제이구구");
            prepareStatement.setString(2, TEST_GAME_PASSWORD);
            return prepareStatement;
        }, keyHolder);
        gameId = keyHolder.getKey().intValue();
    }

    @Test
    void updateTurn() {
        chessDao.updateTurn(Color.BLACK.name(), gameId);

        assertThat(findCurrentColor(gameId)).isEqualTo(Color.BLACK.name());
    }

    private String findCurrentColor(int gameId) {
        return jdbcTemplate.queryForObject("SELECT current_turn FROM game WHERE game_id=?", String.class, gameId);
    }

    @Test
    void deleteAllPiece() {
        insertOnePiece(gameId, DEFAULT_POSITION);

        chessDao.deleteAllPiece(gameId);

        assertThat(getPieceCount(gameId)).isEqualTo(0);
    }

    private void insertOnePiece(int gameId, String originPosition) {
        jdbcTemplate.update("INSERT INTO piece (game_id, piece_name, piece_color, position) VALUES(?,?,?,?)",
                gameId,
                "PAWN",
                "WHITE",
                originPosition
        );
    }

    private int getPieceCount(int gameId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM piece WHERE game_id=?", Integer.class, gameId);
    }

    @Test
    void savePiece() {
        var position = Position.from("c1");
        var piece = PieceFactory.of("PAWN", "BLACK");

        chessDao.savePiece(gameId, new PieceAndPositionDto(position, piece));

        assertThat(getPieceCount(gameId)).isEqualTo(1);
    }

    @Test
    void findAllPiece() {
        var expectedPieces = chessDao.findAllPiece(gameId);
        assertThat(expectedPieces.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장된 게임의 현재 턴의 색상 찾기")
    void findColor() {
        var actualColor = chessDao.findCurrentColor(gameId);
        assertThat(actualColor).isEqualToIgnoringCase(Color.WHITE.name());
    }

    @Test
    void deletePiece() {
        insertOnePiece(gameId, DEFAULT_POSITION);

        chessDao.deletePiece(gameId, DEFAULT_POSITION);

        assertThat(getPieceCount(gameId)).isEqualTo(0);
    }

    @Test
    void updatePiece() {
        insertOnePiece(gameId, DEFAULT_POSITION);

        var toPosition = "A3";

        chessDao.updatePiece(DEFAULT_POSITION, toPosition, gameId);

        var actual = jdbcTemplate.queryForObject("SELECT * FROM piece WHERE position=?",
                (resultSet, rowNum) ->
                        new PieceAndPositionDto(resultSet.getString("piece_name"),
                                resultSet.getString("piece_color"),
                                resultSet.getString("position")
                        ),
                toPosition);

        assertThat(actual.getPosition()).isEqualTo("A3");
    }

    @Test
    void deleteGame() {
        chessDao.deleteGame(gameId);

        assertThat(findAllGameCount()).isEqualTo(0);
    }

    private int findAllGameCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM game", Integer.class);
    }

    @Test
    void initGame() {
        chessDao.initGame("칙촉", TEST_GAME_PASSWORD);

        assertThat(findAllGameCount()).isEqualTo(2);
    }

    @Test
    void findPassword() {
        assertThat(chessDao.findPassword(gameId)).isEqualTo(TEST_GAME_PASSWORD);
    }

    @Test
    void findAllGame() {
        assertThat(chessDao.findAllGame().size()).isEqualTo(1);
    }
}
