package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.entity.Square;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
@SpringBootTest
@Transactional
class SquareImplDaoTest {

    @Autowired
    private RoomImplDao roomDao;

    @Autowired
    private SquareImplDao squareDao;

    @Test
    @DisplayName("방 일련번호와 체스판 구성요소들을 통해 체스보드를 생성할 수 있다.")
    void insertSquareAll() {
        Long roomId = insertTestRoom("title", "1111");
        ChessBoard chessBoard = new ChessBoard(new NormalPiecesGenerator());
        List<Square> board = chessBoard.getPieces().entrySet().stream()
                .map(entry -> new Square(roomId, entry.getKey().toString(),
                        entry.getValue().getSymbol().name(), entry.getValue().getColor().name()))
                .collect(Collectors.toList());

        final int[] squareAllSize = squareDao.insertSquareAll(roomId, board);

        assertThat(squareAllSize).hasSize(64);
    }

    @Test
    @DisplayName("방 일련번호를 통해 체스판 구성요소들을 조회할 수 있다.")
    void findSquareAllById() {
        final Long roomId = insertTestSquareAll();

        final List<Square> squares = squareDao.findSquareAllById(roomId);

        assertThat(squares).hasSize(64);
    }

    @Test
    @DisplayName("방 일련번호를 통해 체스판을 제거할 수 있다.")
    void deleteSquareAllById() {
        assertDoesNotThrow(() -> roomDao.deleteRoom(1L));
    }

    private Long insertTestRoom(String title, String password) {
        return roomDao.insertRoom(title, password);
    }

    private Long insertTestSquareAll() {
        Long roomId = insertTestRoom("title", "1111");
        ChessBoard chessBoard = new ChessBoard(new NormalPiecesGenerator());
        List<Square> board = chessBoard.getPieces().entrySet().stream()
                .map(entry -> new Square(roomId, entry.getKey().toString(),
                        entry.getValue().getSymbol().name(), entry.getValue().getColor().name()))
                .collect(Collectors.toList());
        squareDao.insertSquareAll(roomId, board);
        return roomId;
    }
}
