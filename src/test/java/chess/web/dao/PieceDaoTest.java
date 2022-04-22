package chess.web.dao;

import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PieceDaoTest {

    @Autowired
    private PieceDao pieceDao;
    @Autowired
    private BoardDao boardDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long boardId;
    private Pieces pieces;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS board");

        jdbcTemplate.execute("CREATE TABLE board (" +
                " id   INT(10) not null AUTO_INCREMENT," +
                " turn VARCHAR (5) not null," +
                " primary key (id))");

        jdbcTemplate.execute("CREATE TABLE piece (" +
                " id       INT(10) not null AUTO_INCREMENT," +
                " board_id INT(10)," +
                " position CHAR(2)," +
                " type     VARCHAR (20) not null," +
                " team     VARCHAR (10) not null," +
                " foreign key (board_id) references board (id) ON DELETE CASCADE ," +
                " primary key (id))");

        pieces = Pieces.createInit();
        boardId = boardDao.save();
        pieceDao.save(pieces.getPieces(), boardId);
    }

    @Test
    @DisplayName("새로운 type과 team으로 바꾸면, db에 저장되고 다시 불러왔을 때 바뀐 type과 team이 나온다.")
    void updatePieceByPosition() {
        Piece piece = pieces.getPieces().get(0);
        String newType = "king";
        String newTeam = "black";

        pieceDao.updatePieceByPositionAndBoardId(newType, newTeam, piece.getPosition().name(), boardId);
        List<Piece> updatedPieces = pieceDao.findAllByBoardId(boardId);
        Piece updatedPiece = updatedPieces.get(0);

        assertThat(updatedPiece.getType()).isEqualTo(newType);
        assertThat(updatedPiece.getTeam().value()).isEqualTo(newTeam);
    }

    @Test
    @DisplayName("초기값인 체스말 64개가 모두 조회 되야한다.")
    void findAllByBoardId() {
        //when
        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        //then
        assertThat(pieces.size()).isEqualTo(64);
    }

}
