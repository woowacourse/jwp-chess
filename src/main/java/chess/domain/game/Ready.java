package chess.domain.game;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.Route;

public class Ready extends GameState {

    private static final String STATE = "READY";

    public Ready() {
        super(Board.of(new InitialBoardGenerator()), Color.WHITE);
    }

    public Ready(Board board, Color color) {
        super(board, color);
    }

    @Override
    public GameState start() {
        return new Running(board, turnColor);
    }

    @Override
    public GameState finish() {
        return new Finished(board, turnColor);
    }

    @Override
    public GameState move(Route route) {
        throw new UnsupportedOperationException("[ERROR] 아직 게임이 시작되지 않았습니다..");
    }

    @Override
    public String getState() {
        return STATE;
    }

    @Override
    public boolean isRunnable() {
        return true;
    }
}
