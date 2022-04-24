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

@JdbcTest
class PieceDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PieceDaoImpl pieceDaoImpl;

    @BeforeEach
    void setUp() {
        pieceDaoImpl = new PieceDaoImpl(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("create table piece("
                + "position varchar(2) not null, team varchar(5) not null ,"
                + "name varchar(6) not null, primary key (position))");
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
        pieceDaoImpl.saveAllPieces(board);
        //when
        final List<PieceDto> pieces = pieceDaoImpl.findAllPieces();
        //then
        assertThat(pieces).contains(new PieceDto("a1", "WHITE", "Pawn"))
                .contains(new PieceDto("a2", "BLACK", "Knight"))
                .contains(new PieceDto("a3", "WHITE", "Rook"));
    }

    @Test
    @DisplayName("위치 값과 기물을 받아, 해당 위치 값 데이터를 기물 정보로 업데이트 시킨다.")
    void removeByPosition() {
        //given
        pieceDaoImpl.saveAllPieces(Map.of(Position.from("a2"), new Pawn(BLACK)));
        pieceDaoImpl.removePieceByPosition("a2");
        //when
        final List<PieceDto> pieces = pieceDaoImpl.findAllPieces();
        //then
        assertThat(pieces).doesNotContain(new PieceDto("a2", "WHITE", "Knight"));
    }

    @Test
    @DisplayName("position에 해당 하는 기물 정보를 업데이트한다.")
    void update() {
        //given
        final Piece piece = new Pawn(BLACK);
        pieceDaoImpl.saveAllPieces(Map.of(
                Position.from("a2"), new Pawn(BLACK),
                Position.from("a3"), new Knight(WHITE)));
        pieceDaoImpl.removePieceByPosition("a2");
        pieceDaoImpl.removePieceByPosition("a3");
        pieceDaoImpl.savePiece("a3", piece);
        //when
        final List<PieceDto> actual = pieceDaoImpl.findAllPieces();
        //then
        assertThat(actual).contains(new PieceDto("a3", "BLACK", "Pawn"));
    }
}
