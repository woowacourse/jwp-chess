package wooteco.chess;

import wooteco.chess.controller.ChessConsoleController;
import wooteco.chess.view.InputView;
import wooteco.chess.view.OutputView;

public class ConsoleChessApplication {
    public static void main(String[] args) {
        InputView consoleInputView = new InputView();
        OutputView consoleOutputView = new OutputView();
        ChessConsoleController chessConsoleController = new ChessConsoleController(consoleInputView, consoleOutputView);
        chessConsoleController.run();
    }
}
