package chess.domain.game;

import chess.domain.board.piece.Color;
import chess.domain.event.Event;
import chess.domain.game.statistics.GameResult;
import chess.dto.view.GameSnapshotDto;

public interface Game {

    Game play(Event event);

    boolean isValidTurn(Color playerColor);

    boolean isEnd();

    GameResult getResult();

    GameSnapshotDto toSnapshotDto();
}
