package chess.dao;


import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
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
    void createAllByGameId() {
        pieceDao.createAllByGameId(pieces, gameId);

        assertThat(pieceDao.findAllByGameId(gameId).getPieces()).hasSize(32);
    }

    @DisplayName("findAllByGameId 실행시 해당 gameId에 해당하는 piece들을 반환한다.")
    @Test
    void findAllByGameId() {
        pieceDao.createAllByGameId(pieces, gameId);
        Pieces chessmen = pieceDao.findAllByGameId(gameId);

        assertThat(chessmen.getPieces()).hasSize(32);
    }

    @DisplayName("exists 실행시 gameId에 대해 position에 piece가 존재하는지 확인한다.")
    @Test
    void exists() {
        pieceDao.createAllByGameId(pieces, gameId);
        boolean actual = pieceDao.exists(gameId, "a1");

        assertThat(actual).isTrue();
    }

    @DisplayName("deleteByGameIdAndPosition 실행시 gameId에 대해 position위치의 piece를 삭제한다.")
    @Test
    void deleteByGameIdAndPosition() {
        pieceDao.createAllByGameId(pieces, gameId);
        pieceDao.deleteByGameIdAndPosition(gameId, "a1");

        assertThat(pieceDao.exists(gameId, "a1")).isFalse();
    }

    @DisplayName("updateByGameIdAndPosition 실행시 gameId에 대해 piece의 position을 이동한다.")
    @Test
    void updateByGameIdAndPosition() {
        pieceDao.createAllByGameId(pieces, gameId);
        pieceDao.updateByGameIdAndPosition(gameId, "a2", "a3");

        assertThat(pieceDao.exists(gameId, "a3")).isTrue();
    }

}
