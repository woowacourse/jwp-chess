package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.PieceDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/schema.sql")
class PieceDaoTest {

    private static final int gameId = 1;
    private static final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
    private static final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ChessGameDao chessGameDao;
    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);
        saveGame();
    }

    private void saveGame() {
        chessGameDao.saveChessGame("게임1", "1234", Team.WHITE.getName());
        pieceDao.savePieces(whitePlayer, 1);
        pieceDao.savePieces(blackPlayer, 1);
    }

    @Test
    @DisplayName("체스말을 저정한다.")
    void savePieces() {
        assertDoesNotThrow(() -> pieceDao.savePieces(whitePlayer, gameId));
    }

    @Test
    @DisplayName("게임 번호와 팀명으로 체스말을 조회한다.")
    void findAllPieceByIdAndTeam() {
        final int expected = 16;

        final int actual = pieceDao.findAllPieceByIdAndTeam(gameId, Team.WHITE.getName()).size();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스말을 삭제한다.")
    void deletePieces() {
        final int expected = 0;

        pieceDao.deletePieces(gameId);
        final int whitePiece = pieceDao.findAllPieceByIdAndTeam(gameId, Team.WHITE.getName()).size();
        final int blackPiece = pieceDao.findAllPieceByIdAndTeam(gameId, Team.BLACK.getName()).size();

        assertAll(() -> {
            assertThat(whitePiece).isEqualTo(expected);
            assertThat(blackPiece).isEqualTo(expected);

        });
    }

    @Test
    @DisplayName("특정 위치의 체스말을 삭제한다.")
    void deletePieceByPosition() {
        final String position = "a2";
        final String team = Team.WHITE.getName();

        pieceDao.deletePieceByGameIdAndPositionAndTeam(gameId, position, team);

        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, team);

        assertAll(() -> {
            assertThat(whitePieces.size()).isEqualTo(15);
            assertThat(whitePieces.stream()
                    .anyMatch(pieceDto -> pieceDto.getPosition().equals(position))).isFalse();
        });
    }

    @Test
    @DisplayName("특정 위치로 체스말을 이동한다.")
    void updatePiecePosition() {
        final String current = "a2";
        final String destination = "a4";
        final String team = Team.WHITE.getName();

        pieceDao.updatePiecePositionByGameId(gameId, current, destination, team);

        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, team);

        assertAll(() -> {
            assertThat(whitePieces.size()).isEqualTo(16);
            assertThat(whitePieces.stream()
                    .anyMatch(pieceDto -> pieceDto.getPosition().equals(current))).isFalse();
            assertThat(whitePieces.stream()
                    .anyMatch(pieceDto -> pieceDto.getPosition().equals(destination))).isTrue();
        });
    }
}
