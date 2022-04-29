package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.FakeBoardPieceDao;
import chess.dao.FakeGameDao;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.GameResponse;
import chess.repository.SessionToChessRepository;
import java.util.HashMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

    private ChessService service = new ChessService(new FakeGameDao(), new FakeBoardPieceDao(), new SessionToChessRepository());

    @BeforeEach
    void setUp() {
//        service.clearAll();
        service = new ChessService(new FakeGameDao(), new FakeBoardPieceDao(), new SessionToChessRepository());
    }

    @DisplayName("생성 존재 여부 반환")
    @Test
    void create_and_lastFindChessBoard() {
        boolean beforeSave = service.isExistGame();
        service.saveGame(new SaveRequest("Black Team", new HashMap<>()));
        boolean afterSave = service.isExistGame();

        assertAll(
                () -> assertThat(beforeSave).isEqualTo(false),
                () -> assertThat(afterSave).isEqualTo(true)
        );
    }

    @DisplayName("가장 마지막 데이터를 조회")
    @Test
    void find_lastGame() {
        service.saveGame(new SaveRequest("White Team", new HashMap<>()));
        service.saveGame(new SaveRequest("White Team", new HashMap<>()));
        service.saveGame(new SaveRequest("Black Team", new HashMap<>()));

        GameResponse gameResponse = service.loadLastGame();
        Assertions.assertThat(gameResponse.getTeamName()).isEqualTo("Black Team");
    }
}