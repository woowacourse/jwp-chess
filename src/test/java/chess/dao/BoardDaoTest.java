package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardDaoTest {
    @Autowired
    RoomDao roomDao;
    @Autowired
    GameDao gameDao;
    @Autowired
    BoardDao boardDao;
    private long roomNo;
    private long gameNo;

    @BeforeEach
    void initializeData() {
        roomNo = roomDao.insert(Room.create("title", "password"));
        gameNo = gameDao.insert(ChessGame.create(), roomNo);
    }

    @DisplayName("DB에 초기 보드를 저장한 후 load하면 a1 위치에 흰색 룩이 있다.")
    @Test
    void load_a1_white_rook() {
        Map<Position, Piece> squares = BoardInitializer.get().getSquares();

        boardDao.insert(gameNo, squares);
        List<PieceDto> board = boardDao.load(gameNo);

        PieceDto pieceAtA1 = board.stream()
                .filter(pieceDto -> pieceDto.getPosition().equals("a1"))
                .findAny().get();
        assertThat(pieceAtA1.getCamp() + pieceAtA1.getType()).isEqualTo("whiterook");
    }

    @AfterEach
    void delete() {
        boardDao.delete(gameNo);
        gameDao.delete(roomNo);
        roomDao.delete(roomNo);
    }
}
