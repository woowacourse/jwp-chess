package chess.dao;

import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.PieceDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PieceDao pieceDao;

    private final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
    private final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);
    private final String teamWhite = Team.WHITE.getName();
    private final int gameId = 1;

    @BeforeEach
    void beforeEach() {
        pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.insertAllPieces(whitePlayer, gameId);
    }

    @Test
    @DisplayName("게임 id와 팀 이름을 통해 말들을 조회한다.")
    void findChessGameIdByName() {
        List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, teamWhite);

        assertThat(whitePieces.size()).isEqualTo(16);
    }

    @Test
    @DisplayName("말들을 올바르게 저장한다.")
    void savePieces() {
        pieceDao.insertAllPieces(blackPlayer, gameId);
        List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(gameId, Team.BLACK.getName());

        assertThat(blackPieces.size()).isEqualTo(16);
    }

    @Test
    @DisplayName("말들을 올바르게 삭제한다.")
    void deletePieces() {
        pieceDao.deleteAllPiecesByRoomId(gameId);
        List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, teamWhite);

        assertThat(whitePieces.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 위치의 말을 삭제한다.")
    void deletePieceByGameIdAndPositionAndTeam() {
        String deletePosition = "a2";
        pieceDao.deletePieceByRoomIdAndPositionAndTeam(gameId, deletePosition, teamWhite);
        List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, teamWhite);

        Optional<PieceDto> result = whitePieces.stream()
                .filter(pieceDto -> pieceDto.getPosition().equals(deletePosition))
                .findFirst();

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("말들의 위치를 올바르게 변경한다.")
    void updatePiecePositionByGameId() {
        String currentPosition = "a2";
        String destinationPosition = "a4";
        pieceDao.updatePiecePositionByRoomIdAndTeam(gameId, currentPosition, destinationPosition, teamWhite);
        List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, teamWhite);

        Optional<PieceDto> result = whitePieces.stream()
                .filter(pieceDto -> pieceDto.getPosition().equals(destinationPosition))
                .findFirst();

        assertThat(result.isPresent()).isTrue();
    }

    @AfterEach
    void afterEach() {
        pieceDao.deleteAllPiecesByRoomId(gameId);
    }
}
