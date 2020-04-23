package spring.chess.score;

import spring.chess.game.ChessSet;

public interface Calculatable {
    Score calculate(ChessSet chessSet);
}
