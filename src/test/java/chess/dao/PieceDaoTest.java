package chess.dao;


import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Piece;
import chess.domain.room.Room;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@TestInstance(Lifecycle.PER_CLASS)
@JdbcTest
public class PieceDaoTest {

    private PieceDao pieceDao;
    private List<Piece> pieces;
    private long gameId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        GameDao gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        gameId = gameDao.createByTitleAndPassword(new Room("게임방제목", "password486"));
        pieces = ChessmenInitializer.init().getPieces();
    }

    @DisplayName("createAllByGameId 실행시 해당 gameId에 piece 정보들이 추가된다.")
    @Test
    void createAllById() {
        pieceDao.createAllByGameId(pieces, gameId);

        assertThat(pieceDao.findAllByGameId(gameId).getPieces()).hasSize(32);
    }

}
