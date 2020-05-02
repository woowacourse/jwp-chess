package chess.score;

import chess.player.ChessSet;

public interface Calculatable {
    Score calculate(ChessSet chessSet);
}
