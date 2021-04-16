package chess.controller.console.command;

import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.position.Position;
import chess.service.ChessServiceImpl;
import chess.view.OutputView;

import java.util.List;

public class Move implements Command {
    private static final int MOVE_COMMAND_PROPER_SIZE = 3;
    private static final int FROM_POSITION_INDEX = 1;
    private static final int TO_POSITION_INDEX = 2;

    private final Position from;
    private final Position to;

    public Move(List<String> commands) {
        if (commands.size() != MOVE_COMMAND_PROPER_SIZE) {
            throw new IllegalArgumentException("유효하지 않은 이동 명령입니다.");
        }
        from = Position.of(commands.get(FROM_POSITION_INDEX));
        to = Position.of(commands.get(TO_POSITION_INDEX));
    }

    @Override
    public ChessGameManager execute(ChessServiceImpl chessServiceImpl, long gameId) {
        chessServiceImpl.move(gameId, from, to);
        OutputView.printBoard(chessServiceImpl.findById(gameId).getBoard());
        if (chessServiceImpl.isKindDead(gameId)) {
            chessServiceImpl.end(gameId);
        }
        return chessServiceImpl.findById(gameId);
    }
}
