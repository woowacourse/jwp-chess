package chess.service;

import chess.webdto.ChessGameDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SpringChessServiceTest {

    @Autowired
    SpringChessService springChessService;

    @Test
    void startNewGame() {
        final ChessGameDto chessGameDto = springChessService.startNewGame();
        initialChessGameSettingTest(chessGameDto);
    }

    @Test
    void loadSavedGame() {
        springChessService.startNewGame();
        final ChessGameDto loadedChessGameDto = springChessService.loadSavedGame();
        initialChessGameSettingTest(loadedChessGameDto);
    }

    private void initialChessGameSettingTest(final ChessGameDto chessGameDto) {
        assertThat(chessGameDto.getIsPlaying()).isTrue();

        assertThat(chessGameDto.getCurrentTurnTeam()).isEqualTo(WHITE_TEAM.asDtoFormat());

        final Map<String, Double> teamScore = chessGameDto.getTeamScore();
        assertThat(teamScore.get(WHITE_TEAM.asDtoFormat())).isEqualTo(38.0);
        assertThat(teamScore.get(BLACK_TEAM.asDtoFormat())).isEqualTo(38.0);

        final Map<String, Map<String, String>> piecePositionByTeam = chessGameDto.getPiecePositionByTeam();
        final Map<String, String> whiteTeamPiecePosition = piecePositionByTeam.get(WHITE_TEAM.asDtoFormat());
        assertThat(whiteTeamPiecePosition.get("d1")).isEqualTo("Queen");
        final Map<String, String> blackTeamPiecePosition = piecePositionByTeam.get(BLACK_TEAM.asDtoFormat());
        assertThat(blackTeamPiecePosition.get("e8")).isEqualTo("King");
    }

    @Test
    void move() {
        springChessService.startNewGame();
        final ChessGameDto chessGameDto = springChessService.move("e2", "e4");

        final Map<String, Map<String, String>> piecePositionByTeam = chessGameDto.getPiecePositionByTeam();
        final Map<String, String> whiteTeamPiecePosition = piecePositionByTeam.get(WHITE_TEAM.asDtoFormat());

        assertThat(whiteTeamPiecePosition.get("e4")).isEqualTo("Pawn");
        assertThat(whiteTeamPiecePosition.getOrDefault("e2", "Empty")).isEqualTo("Empty");
    }
}
