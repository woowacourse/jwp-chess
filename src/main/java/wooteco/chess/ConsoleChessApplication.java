package wooteco.chess;

import wooteco.chess.controller.ConsoleChessController;
import wooteco.chess.domain.chessBoard.ChessBoard;
import wooteco.chess.domain.chessBoard.ChessBoardInitializer;
import wooteco.chess.domain.chessGame.ChessCommand;
import wooteco.chess.domain.chessGame.ChessGame;
import wooteco.chess.util.StringUtil;
import wooteco.chess.view.ConsoleInputView;
import wooteco.chess.view.ConsoleOutputView;

import java.util.List;

public class ConsoleChessApplication {

    public static void main(String[] args) {
        ChessBoard chessBoard = new ChessBoard(ChessBoardInitializer.create());
        ChessGame chessGame = ChessGame.from(chessBoard);
        ConsoleChessController consoleChessController = new ConsoleChessController(chessGame);

        ConsoleOutputView.printChessStart();
        if (isStartChessCommand()) {
            consoleChessController.run();
        }
        ConsoleOutputView.printChessEnd();
    }

    private static boolean isStartChessCommand() {
        List<String> commandArguments = StringUtil.splitChessCommand(ConsoleInputView.inputChessCommand());

        if (!ChessCommand.of(commandArguments).isStartChessCommand()) {
            throw new IllegalArgumentException("게임을 시작해야 입력 가능한 명령어입니다.");
        }
        return true;
    }

}
