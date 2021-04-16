package chess.controller.console;

import chess.controller.console.command.Command;
import chess.controller.console.command.CommandRouter;
import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.manager.ChessGameManagerFactory;
import chess.service.ChessServiceImpl;
import chess.util.Repeater;
import chess.view.InputView;
import chess.view.OutputView;

public class ConsoleController {
    private static final int TEMPORARY_GAME_ID = 0;

    private final ChessServiceImpl chessServiceImpl;

    public ConsoleController(ChessServiceImpl chessServiceImpl) {
        this.chessServiceImpl = chessServiceImpl;
    }

    public void run() {
        OutputView.printInitialMessage();
        ChessGameManager chessGameManager = ChessGameManagerFactory.createNotStartedGameManager(TEMPORARY_GAME_ID);
        do {
            Command command = Repeater.repeatOnError(() -> CommandRouter.findByInputCommand(InputView.getCommand()));
            chessGameManager = executeCommandOrPassOnError(chessGameManager.getId(), chessServiceImpl, command);
        } while (chessGameManager.isNotEnd());

        if (chessGameManager.isStart()) {
            OutputView.printResult(chessGameManager.getStatistics());
        }
    }

    private ChessGameManager executeCommandOrPassOnError(long gameId, ChessServiceImpl chessServiceImpl, Command command) {
        try {
            return command.execute(chessServiceImpl, gameId);
        } catch (RuntimeException e) {
            OutputView.printMessage(e.getMessage());
            return chessServiceImpl.findById(gameId);
        }
    }
}
