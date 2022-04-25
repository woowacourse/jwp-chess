package chess.dao;

import chess.dto.ChessGameDto;
import chess.dto.GameInfoDto;
import chess.dto.PieceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChessboardDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ChessboardDao chessboardDao;

    private ChessGameDto chessGameDto = new ChessGameDto(
            List.of(new PieceDto("BLACK", "p", 0, 0)),
            new GameInfoDto("Play", "WHITE")
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
                "    state ENUM('Ready','Play','Finish'),\n" +
                "    turn  ENUM('BLACK','WHITE')\n" +
                ")");
    }

    @Test
    @DisplayName("데이터가 존재한다면 true 반환")
    void isDataExistWhenTrue() {
        jdbcTemplate.update("insert into gameInfos values (?,?)", "Play", "BLACK");
        assertThat(chessboardDao.isDataExist()).isTrue();
    }

    @Test
    @DisplayName("데이터가 존재하지 않는다면 false 반환")
    void isDataExistWhenFalse() {
        assertThat(chessboardDao.isDataExist()).isFalse();
    }

    @Test
    @DisplayName("데이터 저장")
    void save() {
        chessboardDao.save(chessGameDto);
        assertThat(chessboardDao.isDataExist()).isTrue();
    }

    @Test
    @DisplayName("데이터 전체 삭제")
    void truncateAll() {
        chessboardDao.save(chessGameDto);
        chessboardDao.truncateAll();
        assertThat(chessboardDao.isDataExist()).isFalse();
    }

    @Test
    @DisplayName("저장된 데이터를 잘 불러오는지 확인")
    void load() {
        chessboardDao.save(chessGameDto);
        ChessGameDto loadedChessGameDto = chessboardDao.load();

        checkPieces(loadedChessGameDto.getPieces(), chessGameDto.getPieces());
        checkGameInfo(loadedChessGameDto.getGameInfo(), chessGameDto.getGameInfo());
        assertThat(loadedChessGameDto.getState()).isEqualTo(chessGameDto.getState());
        assertThat(loadedChessGameDto.getTurn()).isEqualTo(chessGameDto.getTurn());
    }

    private void checkPieces(List<PieceDto> source, List<PieceDto> target) {
        PieceDto s = source.get(0);
        PieceDto t = target.get(0);

        assertThat(s.getColor()).isEqualTo(t.getColor());
        assertThat(s.getType()).isEqualTo(t.getType());
        assertThat(s.getX()).isEqualTo(t.getX());
        assertThat(s.getY()).isEqualTo(t.getY());
    }

    private void checkGameInfo(GameInfoDto source, GameInfoDto target) {
        assertThat(source.getState()).isEqualTo(target.getState());
        assertThat(source.getTurn()).isEqualTo(target.getTurn());
    }

}
