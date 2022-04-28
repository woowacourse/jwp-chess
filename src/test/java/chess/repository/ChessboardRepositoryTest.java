package chess.repository;

import chess.domain.ChessGame;
import chess.domain.Chessboard;
import chess.domain.Position;
import chess.piece.Color;
import chess.piece.Piece;
import chess.piece.Type;
import chess.utils.PieceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChessboardRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ChessboardRepository chessboardRepository;

    private final ChessGame chessGame = new ChessGame(
            "PLAY",
            "WHITE",
            Map.of(new Position(0, 0), PieceGenerator.generate("p", "BLACK"))
    );

    @BeforeEach
    void setUp() {
        chessboardRepository.truncateAll();
    }

    @Test
    @DisplayName("데이터가 존재한다면 true 반환")
    void isDataExistWhenTrue() {
        jdbcTemplate.update("insert into games(state,turn) values (?,?)", "PLAY", "BLACK");
        assertThat(chessboardRepository.isDataExist()).isTrue();
    }

    @Test
    @DisplayName("데이터가 존재하지 않는다면 false 반환")
    void isDataExistWhenFalse() {
        assertThat(chessboardRepository.isDataExist()).isFalse();
    }

    @Test
    @DisplayName("데이터 저장")
    void save() {
        chessboardRepository.save(chessGame);
        assertThat(chessboardRepository.isDataExist()).isTrue();
    }

    @Test
    @DisplayName("데이터 전체 삭제")
    void truncateAll() {
        chessboardRepository.save(chessGame);
        chessboardRepository.truncateAll();
        assertThat(chessboardRepository.isDataExist()).isFalse();
    }

    @Test
    @DisplayName("저장된 데이터를 잘 불러오는지 확인")
    void load() {
        chessboardRepository.save(chessGame);
        ChessGame loadedChessGame = chessboardRepository.find();

        checkPieces(loadedChessGame.getChessBoard());
        checkGameInfo(loadedChessGame);
    }

    private void checkPieces(Chessboard chessboard) {
        Map<Position, Piece> board = chessboard.getBoard();
        Position position = board.keySet().iterator().next();
        Piece piece = board.get(position);

        assertThat(position.getX()).isEqualTo(0);
        assertThat(position.getY()).isEqualTo(0);
        assertThat(piece.getColor()).isEqualTo(Color.BLACK);
        assertThat(piece.getType()).isEqualTo(Type.PAWN);
    }

    private void checkGameInfo(ChessGame chessGame) {
        assertThat(chessGame.getStateToString()).isEqualTo("PLAY");
        assertThat(chessGame.getColorOfTurn()).isEqualTo("WHITE");
    }

}
