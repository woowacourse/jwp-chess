package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.piece.Color;
import chess.domain.event.Event;
import chess.domain.event.EventType;
import chess.domain.game.statistics.GameResult;
import chess.dto.view.GameSnapshotDto;

public final class NewGame implements Game {

    private static final String GAME_NOT_STARTED_EXCEPTION_MESSAGE = "아직 게임이 시작되지 않았습니다.";

    @Override
    public Game play(Event event) {
        if (!event.hasTypeOf(EventType.INIT)) {
            throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
        }
        Board board = BoardFactory.init();
        return new WhiteTurn(board);
    }

    @Override
    public boolean isValidTurn(Color playerColor) {
        throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public GameResult getResult() {
        throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
    }

    @Override
    public GameSnapshotDto toSnapshotDto() {
        throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
    }
}
