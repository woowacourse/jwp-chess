package chess.dao;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.position.Position;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.dto.PieceDto;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@JdbcTest
class JdbcPieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PieceDao pieceDao;
    private RoomDao roomDao;
    private static final int ROOMNUMBER = 1;

    @BeforeEach
    void setUp() {
        pieceDao = new JdbcPieceDao(jdbcTemplate);
        roomDao = new JdbcRoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("create table room("
                + "id int auto_increment, name varchar(20) not null,"
                + "password varchar(20) not null)");
        jdbcTemplate.execute("create table piece(roomnumber int not null," +
                "position varchar(2) not null," +
                "team varchar(5) not null," +
                "name varchar(6) not null," +
                "foreign key (roomnumber) references room(id)," +
                "primary key (roomnumber, position))");
        roomDao.createRoom("집에 가고 싶다.", "12345678");
    }

    @Test
    @DisplayName("위치에 따른 기물들을 받아 위치, 팀, 이름을 DB에 저장할 수 있다.")
    void saveAll() {
        //given
        final Map<Position, Piece> board = Map.of(
                Position.from("a1"), new Pawn(WHITE),
                Position.from("a2"), new Knight(BLACK),
                Position.from("a3"), new Rook(WHITE)
        );
        pieceDao.saveAllPieces(ROOMNUMBER, board);
        //when
        final List<PieceDto> pieces = pieceDao.findAllPieces(ROOMNUMBER);
        //then
        assertThat(pieces).contains(new PieceDto("a1", "WHITE", "Pawn"))
                .contains(new PieceDto("a2", "BLACK", "Knight"))
                .contains(new PieceDto("a3", "WHITE", "Rook"));
    }

    @Test
    @DisplayName("위치 값과 기물을 받아, 해당 위치 값 데이터를 기물 정보로 업데이트 시킨다.")
    void removeByPosition() {
        //given
        pieceDao.saveAllPieces(ROOMNUMBER, Map.of(Position.from("a2"), new Pawn(BLACK)));
        pieceDao.removePieceByPosition(ROOMNUMBER, "a2");
        //when
        final List<PieceDto> pieces = pieceDao.findAllPieces(ROOMNUMBER);
        //then
        assertThat(pieces).doesNotContain(new PieceDto("a2", "WHITE", "Knight"));
    }

    @Test
    @DisplayName("position에 해당 하는 기물 정보를 업데이트한다.")
    void update() {
        //given
        final Piece piece = new Pawn(BLACK);
        pieceDao.saveAllPieces(ROOMNUMBER, Map.of(
                Position.from("a2"), new Pawn(BLACK),
                Position.from("a3"), new Knight(WHITE)));
        pieceDao.removePieceByPosition(ROOMNUMBER, "a2");
        pieceDao.removePieceByPosition(ROOMNUMBER, "a3");
        pieceDao.savePiece(ROOMNUMBER, "a3", piece);
        //when
        final List<PieceDto> actual = pieceDao.findAllPieces(ROOMNUMBER);
        //then
        assertThat(actual).contains(new PieceDto("a3", "BLACK", "Pawn"));
    }
}
