package spring.chess.score;

import spring.chess.player.ChessSet;

public interface Calculatable {
    Score calculate(ChessSet chessSet);
}
