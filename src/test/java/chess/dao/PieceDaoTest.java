package chess.dao;

import chess.domain.Position;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.NoKingCustomGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.piece.Bishop;
import chess.domain.piece.Knight;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.MoveDto;
import chess.dto.PieceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class PieceDaoTest extends DaoTest {

    private PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDao = new JdbcPieceDao(jdbcTemplate);

        super.setUp();
        jdbcTemplate.execute("CREATE TABLE piece(" +
                "    id       bigint     not null auto_increment primary key,\n" +
                "    roomId   bigint     not null,\n" +
                "    position varchar(3) not null,\n" +
                "    name     varchar(2) not null,\n" +
                "    team     varchar(5) not null,\n" +
                "    constraint fk_roomId foreign key (roomId) references room (id) on delete cascade)"
        );
        jdbcTemplate.execute("insert into room (name, password) values ('chessRoom', 'abcd')");
    }

    @Test
    @DisplayName("db에 초기 화이트 체스말들이 세팅되는지 확인한다.")
    void initializeWhitePieces() {
        pieceDao.initializePieces(1L, new Player(new WhiteGenerator(), Team.WHITE));
        final String sql = "select count(*) from piece where roomId = 1 and team = 'WHITE'";
        final int expected = 16;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에 초기 블랙 체스말들이 세팅되는지 확인한다.")
    void initializeBlackPieces() {
        pieceDao.initializePieces(1L, new Player(new BlackGenerator(), Team.BLACK));
        final String sql = "select count(*) from piece where roomId = 1 and team = 'BLACK'";
        final int expected = 16;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 팀의 체스말들을 모두 찾는다.")
    void findPiecesByTeam() {
        pieceDao.initializePieces(1L, new Player(new NoKingCustomGenerator(), Team.WHITE));

        final List<PieceDto> actual = pieceDao.findPiecesByTeam(1L, Team.WHITE);

        assertThat(actual).contains(
                PieceDto.from(new Rook(Position.of(1, 'a'))),
                PieceDto.from(new Rook(Position.of(1, 'h'))),
                PieceDto.from(new Knight(Position.of(1, 'b'))),
                PieceDto.from(new Knight(Position.of(1, 'g'))),
                PieceDto.from(new Bishop(Position.of(1, 'c'))),
                PieceDto.from(new Bishop(Position.of(1, 'f'))),
                PieceDto.from(new Queen(Position.of(1, 'd')))
        );
    }

    @Test
    @DisplayName("db에서 체스말의 위치를 업데이트해준다.")
    void updatePiece() {
        pieceDao.initializePieces(1L, new Player(new WhiteGenerator(), Team.WHITE));
        final MoveDto moveDto = new MoveDto("a2", "a4");
        pieceDao.updatePiece(1L, moveDto);
        final String sql = "select count(*) from piece where roomId = 1 and position = 'a4'";
        final int expected = 1;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("capture가 일어나는 경우 db에서 체스말을 삭제시켜준다.")
    void removePieceByCaptured() {
        pieceDao.initializePieces(1L, new Player(new NoKingCustomGenerator(), Team.WHITE));
        pieceDao.initializePieces(1L, new Player(new BlackGenerator(), Team.BLACK));
        final MoveDto moveDto = new MoveDto("a1", "a7");
        pieceDao.removePieceByCaptured(1L, moveDto);
        final String sql = "select count(*) from piece where roomId = 1 and position = 'a7'";
        final int expected = 0;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 모든 체스말을 삭제시켜준다.")
    void endPieces() {
        pieceDao.initializePieces(1L, new Player(new WhiteGenerator(), Team.WHITE));
        pieceDao.endPieces(1L);
        final String sql = "select count(*) from piece where roomId = 1";
        final int expected = 0;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }
}
