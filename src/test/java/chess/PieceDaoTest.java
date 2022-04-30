package chess;

import chess.entity.PieceEntity;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class PieceDaoTest {
    PieceDao pieceDao;
    GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initPieceDaoTest() {
        jdbcTemplate.execute("DROP TABLE PIECES IF EXISTS");
        jdbcTemplate.execute("DROP TABLE GAMES IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE games\n" +
                "(\n" +
                "    game_id  int         not null AUTO_INCREMENT,\n" +
                "    name     varchar(64) not null,\n" +
                "    password varchar(64) not null,\n" +
                "    turn     varchar(5)  not null,\n" +
                "    primary key (game_id)\n" +
                ");");
        gameDao = new GameDao(jdbcTemplate);
        jdbcTemplate.execute("CREATE TABLE pieces\n" +
                "(\n" +
                "    piece_id int         not null AUTO_INCREMENT,\n" +
                "    position varchar(4)  not null,\n" +
                "    name     varchar(10) not null,\n" +
                "    game_id int not null,\n" +
                "    primary key (piece_id),\n" +
                "    foreign key(game_id) references GAMES (game_id)\n" +
                ");");

        pieceDao = new PieceDao(jdbcTemplate);
    }

    @AfterEach
    void cleanDB() {
        pieceDao.deleteAll();
    }

    @Test
    @DisplayName("체스판이 db에 저장되었는지 확인한다")
    void init() {
        long gameId = gameDao.initGame("room", "1234");
        pieceDao.init(BoardFactory.create(), gameId);
        List<PieceEntity> boardMap = pieceDao.findAllByGameId(gameId);

        assertThat(boardMap.size()).isEqualTo(64);
    }

    @Test
    @DisplayName("게임 고유 번호에 대한 체스판의 말을 모두 삭제한다.")
    void deleteByGameId() {
        long gameId = gameDao.initGame("room", "1234");
        pieceDao.init(BoardFactory.create(), gameId);

        pieceDao.deleteByGameId(gameId);

        List<PieceEntity> boardMap = pieceDao.findAllPieces();
        assertThat(boardMap.size()).isZero();
    }

    @Test
    @DisplayName("체스판에서 위치와 게임 아이디로 piece의 이름을 찾아온다.")
    void findPieceNameByPositionAndGameId() {
        long gameId = gameDao.initGame("room1", "1234");
        pieceDao.init(BoardFactory.create(), gameId);
        String pieceName = pieceDao.findPieceNameByPositionAndGameId("a2", gameId);

        assertThat(pieceName).isEqualTo("white-p");
    }
}
