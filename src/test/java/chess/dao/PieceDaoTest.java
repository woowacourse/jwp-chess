package chess.dao;

import chess.domain.ChessGame;
import chess.dto.ChessGameDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PieceDaoTest {
    private static final String PASSWORD = "password";
    @Autowired
    private ChessGameDao chessGameDao;
    @Autowired
    private PieceDao pieceDao;

    @AfterEach
    void rollback() {
        chessGameDao.remove("test");
    }

    @DisplayName("피스들 저장 테스트")
    @Test
    public void save() {
        //given
        ChessGame chessGame = new ChessGame("test");
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        //when
        Long id = chessGameDao.save(chessGameDto, PASSWORD);

        //then
        Assertions.assertDoesNotThrow(() -> pieceDao.save(id, chessGameDto));
    }
}
