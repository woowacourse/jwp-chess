package chess.dao;

import chess.domain.piece.Color;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.dto.PieceAndPositionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class ChessDaoTest {

    private final List<PieceAndPositionDto> pieceAndPositionDtos = List.of(
            new PieceAndPositionDto(Position.from("A1"), PieceFactory.of("KING", "BLACK")),
            new PieceAndPositionDto(Position.from("A2"), PieceFactory.of("PAWN", "BLACK")),
            new PieceAndPositionDto(Position.from("B1"), PieceFactory.of("KING", "WHITE")),
            new PieceAndPositionDto(Position.from("B2"), PieceFactory.of("PAWN", "WHITE"))
    );
    private final int testGameId = 1;
    private ChessDaoImpl chessDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessDao = new ChessDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");

        jdbcTemplate.execute("CREATE TABLE game (" +
                "game_id INT PRIMARY KEY NOT NULL," +
                "current_turn VARCHAR(10) DEFAULT'white'" +
                ");");
        jdbcTemplate.execute("CREATE TABLE piece (" +
                "piece_id INT PRIMARY KEY AUTO_INCREMENT," +
                "game_id INT NOT NULL," +
                "piece_name VARCHAR(10) NOT NULL," +
                "piece_color VARCHAR(10) NOT NULL," +
                "position VARCHAR(2) NOT NULL," +
                "FOREIGN KEY (game_id) REFERENCES game(game_id)" +
                ");");

        jdbcTemplate.update("INSERT INTO game (game_id) VALUES(?)", ChessDaoImpl.DEFAULT_GAME_ID);

        initTestPiece();
    }

    private void initTestPiece() {
        final var sql = "INSERT INTO piece (game_id, piece_name, piece_color, position) VALUES(?,?,?,?)";

        for (final PieceAndPositionDto pieceAndPositionDto : pieceAndPositionDtos) {
            jdbcTemplate.update(sql, ChessDaoImpl.DEFAULT_GAME_ID, pieceAndPositionDto.getPieceName(), pieceAndPositionDto.getPieceColor(), pieceAndPositionDto.getPosition());
        }
    }

    @Test
    void updateTurn() {
        chessDao.updateTurn(Color.BLACK.name(), ChessDaoImpl.DEFAULT_GAME_ID);

        final var currentColor = chessDao.findCurrentColor(ChessDaoImpl.DEFAULT_GAME_ID);

        assertThat(currentColor).isEqualTo(Color.BLACK.name());
    }

    @Test
    void deleteAllPiece() {
        final var beforeCount = getPieceCount();
        assertThat(beforeCount).isEqualTo(4);

        chessDao.deleteAllPiece(ChessDaoImpl.DEFAULT_GAME_ID);

        final var afterCount = getPieceCount();
        assertThat(afterCount).isEqualTo(0);
    }

    private Integer getPieceCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM piece WHERE game_id = ?", Integer.class, ChessDaoImpl.DEFAULT_GAME_ID);
    }

    @Test
    void savePiece() {
        final var beforeCount = getPieceCount();
        assertThat(beforeCount).isEqualTo(4);

        chessDao.savePiece(ChessDaoImpl.DEFAULT_GAME_ID, new PieceAndPositionDto(Position.from("c1"), PieceFactory.of("PAWN", "BLACK")));

        final var afterCount = getPieceCount();
        assertThat(afterCount).isEqualTo(5);
    }

    @Test
    void findAllPiece() {
        var expectedPieces = chessDao.findAllPiece(ChessDaoImpl.DEFAULT_GAME_ID);
        assertThat(pieceAndPositionDtos).isEqualTo(expectedPieces);
    }

    @Test
    void findCurrentColor() {
        var actualColor = chessDao.findCurrentColor(ChessDaoImpl.DEFAULT_GAME_ID);
        assertThat(actualColor).isEqualToIgnoringCase(Color.WHITE.name());
    }

    @Test
    void deletePiece() {
        var before = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM piece WHERE position = 'A1'", Integer.class);

        assertThat(before).isEqualTo(1);

        chessDao.deletePiece(ChessDaoImpl.DEFAULT_GAME_ID, "A1");

        var after = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM piece WHERE position = 'A1'", Integer.class);

        assertThat(after).isEqualTo(0);
    }

    @Test
    void updatePiece() {
        chessDao.updatePiece("A1", "A3", ChessDaoImpl.DEFAULT_GAME_ID);

        var actual = jdbcTemplate.queryForObject("SELECT * FROM piece WHERE position = 'A3'", (resultSet, rowNum) ->
                new PieceAndPositionDto(resultSet.getString("piece_name"),
                        resultSet.getString("piece_color"),
                        resultSet.getString("position")
                ));

        assertThat(actual).isEqualTo(new PieceAndPositionDto(Position.from("A3"), PieceFactory.of("KING", "BLACK")));
    }

    @Test
    void deleteGame() {
        jdbcTemplate.update("INSERT INTO game (game_id) VALUES(?)", testGameId);

        var before = getGameCount();
        assertThat(before).isEqualTo(2);

        chessDao.deleteGame(testGameId);

        var after = getGameCount();

        assertThat(after).isEqualTo(1);
    }

    private Integer getGameCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM game", Integer.class);
    }

    @Test
    void initGame() {
        var before = getGameCount();
        assertThat(before).isEqualTo(1);

        chessDao.initGame(testGameId);

        var after = getGameCount();
        assertThat(after).isEqualTo(2);
    }
}
