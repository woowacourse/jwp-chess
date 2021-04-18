package chess.repository;

import chess.dao.ChessDao;
import chess.entity.Chess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JDBCChessDaoTest {

    @Autowired
    private ChessDao chessDao;

    @Test
    void name() {
        String test = "테스트";
        Chess chess = new Chess(test);

        chessDao.save(chess);
    }
}