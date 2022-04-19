package chess.domain.game;

import chess.domain.event.Event;
import chess.domain.game.statistics.GameResult;
import chess.dto.GameDto;

public interface Game {

    Game play(Event event);

    boolean isEnd();

    GameResult getResult();

    GameDto toDtoOf(int gameId);
}
