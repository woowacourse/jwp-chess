package chess.repository;

import chess.chessgame.ChessGame;
import chess.chessgame.Position;
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

    private ChessGame chessGame = new ChessGame(
            "PLAY",
            "WHITE",
            Map.of(new Position(0, 0), PieceGenerator.generate("p", "BLACK"))
    );

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS pieces");
        jdbcTemplate.execute("DROP TABLE IF EXISTS gameInfos");

        jdbcTemplate.execute("CREATE TABLE pieces\n" +
                "(\n" +
                "    piece_index int NOT NULL AUTO_INCREMENT,\n" +
                "    type        ENUM('k','q','b','n','r','p','.'),\n" +
                "    color       ENUM('BLACK','WHITE','NONE'),\n" +
                "    x           int NOT NULL,\n" +
                "    y           int NOT NULL,\n" +
                "    PRIMARY KEY (piece_index)\n" +
                ")");

        jdbcTemplate.execute("CREATE TABLE gameInfos\n" +
                "(\n" +
                "    state ENUM('READY','PLAY','FINISH'),\n" +
                "    turn  ENUM('BLACK','WHITE')\n" +
                ")");
    }

    @Test
    @DisplayName("데이터가 존재한다면 true 반환")
    void isDataExistWhenTrue() {
        jdbcTemplate.update("insert into gameInfos values (?,?)", "PLAY", "BLACK");
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
        ChessGame loadedChessGame = chessboardRepository.load();

        checkPieces(loadedChessGame.getChessBoard());
        checkGameInfo(loadedChessGame);
    }

    private void checkPieces(Map<Position, Piece> pieces) {
        Position position = pieces.keySet().iterator().next();
        Piece piece = pieces.get(position);

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
