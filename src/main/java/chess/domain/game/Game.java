package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.event.Event;
import chess.domain.game.statistics.GameResult;
import chess.domain.game.statistics.GameState;

public interface Game {

    Game play(Event event);

    boolean isEnd();

    GameResult getResult();

    Board getBoard();

    GameState getState();
}
