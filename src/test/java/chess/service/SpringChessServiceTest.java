package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.piece.Color;
import chess.domain.piece.MoveResult;
import chess.dto.GameCreateRequest;
import chess.dto.GameCreateResponse;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import chess.dto.MoveResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringChessServiceTest {

    @Autowired
    private SpringChessService springChessService;

    private List<Integer> gameIds = new ArrayList<>();

    @BeforeEach
    void setup() {
        for (int i = 1; i <= 3; i++) {
            final GameCreateRequest gameRequest = new GameCreateRequest(
                    "test room" + i, "password", "white", "black");
            final GameCreateResponse gameCreateResponse = springChessService.create(gameRequest);
            gameIds.add(gameCreateResponse.getId());
        }
    }

    @AfterEach
    void clear() {
        springChessService.deleteAll();
        gameIds = new ArrayList<>();
    }

    @Test
    @DisplayName("방제목, 비밀번호, 양팀 닉네임으로 게임을 생성할 수 있다.")
    void create() {
        final GameCreateResponse gameCreateResponse = springChessService.create(
                new GameCreateRequest("room", "password", "white", "black"));

        final int id = gameCreateResponse.getId();
        assertThat(springChessService.findById(id)).isNotNull();
    }

    @Test
    @DisplayName("게임 전체 목록을 조회할 수 있다.")
    void findAll() {
        final List<GameDto> all = springChessService.findAll();
        assertThat(all.size()).isEqualTo(gameIds.size());
    }

    @Test
    @DisplayName("게임 아이디로 단일 게임을 조회할 수 있다.")
    void findById() {
        final int targetId = gameIds.get(0);
        final GameDto gameDto = springChessService.findById(targetId);
        assertThat(gameDto.getRoomName()).isEqualTo("test room1");
    }

    @Test
    @DisplayName("게임 아이디로 점수를 조회할 수 있다.")
    void findScoreById() {
        final int targetId = gameIds.get(0);
        final Map<Color, Double> scoreById = springChessService.findScoreById(targetId);
        assertAll(
                () -> assertThat(scoreById.get(Color.WHITE)).isEqualTo(38.0),
                () -> assertThat(scoreById.get(Color.BLACK)).isEqualTo(38.0)
        );
    }

    @Test
    @DisplayName("기물을 이동한 정보를 저장할 수 있다")
    void updateBoard() {
        final int targetId = gameIds.get(0);
        final MoveResponse moveResponse = springChessService.updateBoard(targetId,
                new MoveRequest("pw", "E2", "E4", targetId));

        assertAll(
                () -> assertThat(moveResponse.getMoveResult()).isEqualTo(MoveResult.SUCCESS),
                () -> assertThat(moveResponse.getFrom()).isEqualTo("E2"),
                () -> assertThat(moveResponse.getTo()).isEqualTo("E4"),
                () -> assertThat(moveResponse.getPiece()).isEqualTo("pw")
        );
    }
}
