package chess.application.console;

import chess.view.InputView;
import chess.view.OutputView;

public class Application {

    public static void main(String[] args) {
        ChessGame chessGame = new ChessGame(new OutputView(), new InputView());
        chessGame.run();
    }
}