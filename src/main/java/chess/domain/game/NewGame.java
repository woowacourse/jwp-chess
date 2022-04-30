package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.event.Event;
import chess.domain.game.statistics.GameResult;
import chess.domain.game.statistics.GameState;
import chess.util.BoardMapGeneratorUtil;

public final class NewGame implements Game {

    private static final String GAME_NOT_STARTED_EXCEPTION_MESSAGE = "아직 게임이 시작되지 않았습니다.";

    @Override
    public Game play(Event event) {
        if (!event.isInit()) {
            throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
        }
        Board board = new Board(BoardMapGeneratorUtil.initFullChessBoard());
        return new WhiteTurn(board);
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
    public Board getBoard() {
        throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
    }

    @Override
    public GameState getState() {
        throw new UnsupportedOperationException(GAME_NOT_STARTED_EXCEPTION_MESSAGE);
    }
}
