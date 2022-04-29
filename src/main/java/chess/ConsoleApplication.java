package chess;

import chess.controller.console.ConsoleController;

public class ConsoleApplication {

    public static void main(String[] args) {
        ConsoleController gameController = new ConsoleController();
        gameController.run();
    }
}
