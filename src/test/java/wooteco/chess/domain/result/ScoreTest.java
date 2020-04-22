package wooteco.chess.domain.result;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.player.PlayerColor;

class ScoreTest {

    private Map<GamePiece, Integer> piecesCount;

    @BeforeEach
    void setUp() {
        piecesCount = new HashMap<>();
        piecesCount.put(new Knight(PlayerColor.BLACK), 2);
        piecesCount.put(new Bishop(PlayerColor.BLACK), 1);
        piecesCount.put(new Pawn(PlayerColor.BLACK), 2);
    }

    @Test
    void create() {
        int sameFilePawnCount = 2;
        Score score = Score.of(piecesCount, sameFilePawnCount);

        assertThat(score.getScore()).isEqualTo(9);
    }
}