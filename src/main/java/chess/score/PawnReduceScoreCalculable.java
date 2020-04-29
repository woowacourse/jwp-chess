package chess.score;

import chess.location.Col;
import chess.player.ChessSet;

import static chess.board.ChessBoardCreater.COL_END;
import static chess.board.ChessBoardCreater.COL_START;

public class PawnReduceScoreCalculable implements Calculatable {
    private static final double PAWN_REDUCE_VALUE = 0.5;

    @Override
    public Score calculate(ChessSet chessSet) {
        double reducePawnScore = 0;
        for (int col = COL_START; col <= COL_END; col++) {
            Col fixCol = Col.of(col);

            int sameColPawnSize = chessSet.calculateExistPawnSizeInSame(fixCol);
            if (sameColPawnSize == 1) {
                continue;
            }
            reducePawnScore += (sameColPawnSize * PAWN_REDUCE_VALUE);
        }
        return new Score(reducePawnScore);
    }
}
