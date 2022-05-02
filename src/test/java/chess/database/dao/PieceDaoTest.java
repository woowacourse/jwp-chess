package chess.database.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.database.entity.GameEntity;
import chess.database.entity.PieceEntity;
import chess.database.entity.PointEntity;
import chess.database.entity.RoomEntity;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.game.Ready;
import chess.domain.piece.PieceType;

@JdbcTest
class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RoomDao roomDao;
    private GameDao gameDao;
    private PieceDao dao;

    private Long roomId;
    private Long gameId;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(dataSource, jdbcTemplate);
        roomId = roomDao.saveRoom(RoomEntity.from("TEST-ROOM-NAME", "TEST-PASSWORD"));

        gameDao = new JdbcGameDao(dataSource, jdbcTemplate);
        gameId = gameDao.saveGame(GameEntity.fromRoomId(new Ready(), roomId));

        dao = new JdbcPieceDao(dataSource, jdbcTemplate);
        Board board = Board.of(new InitialBoardGenerator());
        dao.saveBoard(board.getPointPieces()
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().isSameType(PieceType.EMPTY))
            .map(entry -> PieceEntity.from(entry, gameId))
            .collect(Collectors.toList()));
    }

    @Test
    @DisplayName("말의 위치와 종류를 저장한다.")
    public void insert() {
        // given & when
        Board board = Board.of(new InitialBoardGenerator());
        // then
        assertThatCode(
            () -> dao.saveBoard(board.getPointPieces()
                .entrySet()
                .stream()
                .map(entry -> PieceEntity.from(entry, gameId))
                .collect(Collectors.toList()))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말의 위치와 종류를 조회한다.")
    public void select() {
        // given & when
        final List<PieceEntity> pieceEntities = dao.findBoardByGameId(gameId);
        // then
        assertThat(pieceEntities.size()).isEqualTo(32);
    }

    @Test
    @DisplayName("말의 위치를 움직인다.")
    public void update() {
        // given & when
        PointEntity source = new PointEntity(1, 2);
        PointEntity destination = new PointEntity(1, 4);
        // then
        assertThatCode(() -> dao.updatePiece(source, destination, gameId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말을 삭제한다.")
    public void delete() {
        // given & when
        PointEntity entity = new PointEntity(1, 2);

        // then
        assertThatCode(() -> dao.deletePiece(entity, gameId)).doesNotThrowAnyException();
    }

    @AfterEach
    void setDown() {
        roomDao.deleteRoom(roomId);
    }
}