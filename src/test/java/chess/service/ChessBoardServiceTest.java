package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.PieceEntity;
import chess.model.GameResult;
import chess.model.Team;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.WebBoardDto;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"/initGames.sql", "/initPieces.sql"})
class ChessBoardServiceTest {

    private static final Long gameId = 1L;

    private final JdbcTemplate jdbcTemplate;
    private final ChessBoardService chessBoardService;

    @Autowired
    ChessBoardServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        chessBoardService = new ChessBoardService(new PieceDao(jdbcTemplate), new GameDao(jdbcTemplate));
    }

    @BeforeEach
    void beforeEach() {
        String sql = "insert into pieces (position, name, game_id) values (?, ?, ?)";

        List<Object[]> batch = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : BoardFactory.create().getBoard().entrySet()) {
            Object[] values = new Object[]{entry.getKey().getPosition(),
                    (entry.getValue().getTeam().name() + "-" + entry.getValue().getName()).toLowerCase(),
                    gameId};
            batch.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batch);
    }

    @Test
    @DisplayName("엔티티로 받은 데이터를 Board로 변환하는 테스트")
    void toBoardTest() {
        WebBoardDto pieces = WebBoardDto.from(BoardFactory.create());
        List<PieceEntity> pieceEntities = pieces.getWebBoard().entrySet().stream()
                .map(piece -> new PieceEntity(piece.getValue(), piece.getKey()))
                .collect(Collectors.toList());

        Board resultBoard = ChessBoardService.toBoard(pieceEntities);

        assertThat(resultBoard).isEqualTo(BoardFactory.create());
    }

    @Test
    @DisplayName("왕의 생존 여부를 반환받는 테스트")
    void isKingDeadTest() {
        boolean isKingDead = chessBoardService.isKingDead(gameId);

        assertThat(isKingDead).isFalse();
    }

    @Test
    @DisplayName("게임 결과 반환 테스트")
    void getGameResultTest() {
        GameResult gameResult = chessBoardService.getResult(gameId);

        assertThat(gameResult.getWhiteScore()).isEqualTo(38);
        assertThat(gameResult.getBlackScore()).isEqualTo(38);
        assertThat(gameResult.getWinningTeam()).isEqualTo(Team.NONE);
    }
}