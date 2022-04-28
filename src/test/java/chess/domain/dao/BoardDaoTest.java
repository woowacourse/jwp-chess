package chess.domain.dao;

import chess.service.dto.PieceDto;
import chess.domain.game.board.ChessBoardFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BoardDaoTest {

    private final BoardDao boardDao;
    private final GameDao gameDao;

    public BoardDaoTest(BoardDao boardDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
    }
    @BeforeEach
    void set() {
        boardDao.deleteAll();
        gameDao.deleteAll();
        gameDao.save(ChessBoardFactory.initBoard());
    }

    @Test
    @DisplayName("체스 기물을 저장한다.")
    void save() {
        //when
        boardDao.save(1, "a1", "Pawn", "WHITE");
        //then
        assertThat(boardDao.findByGameId(1).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("가장 최근 게임의 기물 정보를 삭제한다.")
    void delete() {
        //given
        boardDao.save(gameDao.findLastGameId(), "a1", "Pawn", "WHITE");
        boardDao.save(gameDao.findLastGameId(), "a2", "Pawn", "WHITE");
        //when
        int lastGameId = gameDao.findLastGameId();
        boardDao.delete(lastGameId);
        //then
        assertThat(boardDao.findByGameId(lastGameId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 id로 기물을 찾는다.")
    void findByGameId() {
        //given
        boardDao.save(gameDao.findLastGameId(), "a1", "Pawn", "WHITE");
        boardDao.save(gameDao.findLastGameId(), "a2", "Pawn", "WHITE");
        //when
        List<PieceDto> actual = boardDao.findByGameId(gameDao.findLastGameId());
        //then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getGameId()).isEqualTo(1);
        assertThat(actual.get(0).getPosition()).isEqualTo("a1");
        assertThat(actual.get(0).getPiece()).isEqualTo("Pawn");
    }

    @AfterEach
    void tearDown() {
        boardDao.deleteAll();
        gameDao.deleteAll();
    }
}