package wooteco.chess.domain.piece.implementation;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.implementation.piece.Knight;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

class KnightTest {

    private PieceState whiteKnight;
    private BoardSituation boardSituation;
    private Map<Position, Team> boardDto;
    private Team whiteTeam = Team.WHITE;
    private Team blackTeam = Team.BLACK;

    @BeforeEach
    void setUp() {
        whiteKnight = Knight.of(Position.of("b1"), wooteco.chess.domain.player.Team.WHITE);
        boardDto = new HashMap<>();
        boardSituation = BoardSituation.of(boardDto);
    }

    @Test
    @DisplayName("진행 타겟에 우리편이 있는 경우 예외 발생")
    void moveToAlly() {
        boardDto.put(Position.of("C3"), whiteTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whiteKnight.move(Position.of("C3"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("진행 타겟에 적군이 있는 경우 이동 가능")
    void moveToEnemy() {
        boardDto.put(Position.of("C3"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThat(whiteKnight.move(Position.of("C3"), boardSituation))
            .isInstanceOf(Knight.class);
    }

    @Test
    @DisplayName("진행 타겟에 아무것도 없는 경우 이동 가능")
    void moveToEmpty() {
        assertThat(whiteKnight.move(Position.of("C3"), boardSituation))
            .isInstanceOf(Knight.class);
    }

    @Test
    @DisplayName("진행 규칙에 어긋나는 경우 예외 발생")
    void movePolicyException() {
        assertThatThrownBy(() -> whiteKnight.move(Position.of("C4"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("진행 타겟에 적군이 있지만 진행 규칙에 어긋나는 경우 예외 발생")
    void moveToEnemyException() {
        boardDto.put(Position.of("C4"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whiteKnight.move(Position.of("C4"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
