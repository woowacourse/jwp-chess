package chess.dao.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.game.Room;
import chess.domain.gamestate.running.Ready;
import chess.domain.location.Location;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.team.Team;
import chess.dao.room.JdbcRoomDao;
import chess.utils.BoardUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JdbcPieceDaoTest {

    @Autowired
    private JdbcPieceDao jdbcPieceDao;

    @Autowired
    private JdbcRoomDao jdbcRoomDao;

    private long roomId;

    @BeforeEach
    void setUp() {
        Room room = new Room(1, "Piece테스트", new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        roomId = jdbcRoomDao.insert(room);
    }

    @Test
    void insert() {
        // given
        Queen piece = Queen.of(Location.of(1, 1), Team.WHITE);

        // when
        long uid = jdbcPieceDao.insert(roomId, piece);
        Piece foundPiece = jdbcPieceDao.findPieceById(uid);

        // then
        assertThat(foundPiece).isInstanceOf(Queen.class);
        assertAll(
            () -> assertThat(foundPiece.getId()).isEqualTo(uid),
            () -> assertThat(foundPiece.getRoomId()).isEqualTo(roomId),
            () -> assertThat(foundPiece.getTeam()).isEqualTo(piece.getTeam()),
            () -> assertThat(foundPiece.getSignature()).isEqualTo(piece.getSignature()),
            () -> assertThat(foundPiece.getLocation()).isEqualTo(Location.of(1, 1))
        );
    }

    @Test
    void update() {
        // given
        Queen original = Queen.of(Location.of(1, 1), Team.WHITE);
        long pieceId = jdbcPieceDao.insert(roomId, original);

        // when
        Queen updated = Queen.of(pieceId, roomId, Team.WHITE, Location.of(1, 8));
        jdbcPieceDao.update(updated);
        Piece foundPiece = jdbcPieceDao.findPieceById(pieceId);

        // then
        assertThat(foundPiece).isInstanceOf(Queen.class);
        assertThat(foundPiece.getLocation()).isEqualTo(Location.of(1, 8));
    }

    @Test
    void findPiecesByRoomId() {
        // given
        Queen piece1 = Queen.of(Location.of(1, 1), Team.WHITE);
        King piece2 = King.of(Location.of(1, 2),Team.WHITE);

        // when
        long piece1Id = jdbcPieceDao.insert(roomId, piece1);
        long piece2Id = jdbcPieceDao.insert(roomId, piece2);
        List<Piece> foundPieces = jdbcPieceDao.findPiecesByRoomId(roomId);

        // then
        assertThat(foundPieces.size()).isEqualTo(2);
        assertAll(
            () -> assertThat(foundPieces.get(0).getRoomId()).isEqualTo(roomId),
            () -> assertThat(foundPieces.get(0).getId()).isEqualTo(piece1Id),
            () -> assertThat(foundPieces.get(0).getSignature()).isEqualTo(piece1.getSignature()),
            () -> assertThat(foundPieces.get(0).getTeam()).isEqualTo(piece1.getTeam()),
            () -> assertThat(foundPieces.get(0).getLocation()).isEqualTo(Location.of(1, 1)),
            () -> assertThat(foundPieces.get(1).getRoomId()).isEqualTo(roomId),
            () -> assertThat(foundPieces.get(1).getId()).isEqualTo(piece2Id),
            () -> assertThat(foundPieces.get(1).getSignature()).isEqualTo(piece2.getSignature()),
            () -> assertThat(foundPieces.get(1).getTeam()).isEqualTo(piece2.getTeam()),
            () -> assertThat(foundPieces.get(1).getLocation()).isEqualTo(Location.of(1, 2))
        );
    }

    @Test
    void count() {
        // given, when
        int zero = jdbcPieceDao.count();

        jdbcPieceDao.insert(roomId, Queen.of(Location.of("a1"), Team.WHITE));
        int one = jdbcPieceDao.count();

        // then
        assertThat(zero).isEqualTo(0);
        assertThat(one).isEqualTo(1);
    }

    @Test
    void deleteAll() {
        jdbcPieceDao.deleteAll();
        assertThat(jdbcPieceDao.count()).isEqualTo(0);
    }

    @Test
    void deletePieceById() {
        // given
        Queen piece = Queen.of(Location.of(1, 1), Team.WHITE);

        // when
        long id = jdbcPieceDao.insert(roomId, piece);
        jdbcPieceDao.deletePieceById(id);

        // then
        assertThatThrownBy(() -> jdbcPieceDao.findPieceById(id))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
