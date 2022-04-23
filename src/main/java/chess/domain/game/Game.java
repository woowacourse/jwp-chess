package chess.domain.game;

import chess.domain.event.Event;
import chess.domain.game.statistics.GameResult;
import chess.dto.GameSnapshotDto;

public interface Game {

    Game play(Event event);

    boolean isEnd();

    GameResult getResult();

    GameSnapshotDto toDtoOf(int gameId);
}
