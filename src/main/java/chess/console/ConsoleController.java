package chess.console;

import chess.domain.ChessGame;
import chess.domain.command.Command;
import chess.domain.command.Commands;
import chess.dto.BoardDto;
import chess.dto.PointDto;
import chess.view.InputView;
import chess.view.OutputView;

public class ConsoleController {

    public void run() {
        ChessGame chessGame = new ChessGame();
        OutputView.printCommandInfo();
        while (chessGame.isRunning()) {
            final Commands inputCmd = InputView.inputCommand();
            Command command = Command.of(inputCmd.mainCommand());
            command.apply(chessGame, inputCmd);
            view(chessGame, command);
        }
    }

    private void view(ChessGame chessGame, Command command) {
        if (command.isStart()) {
            OutputView.printBoard(new BoardDto(chessGame.board(), chessGame.turn()));
        }
        if (command.isMove()) {
            OutputView.printBoard(new BoardDto(chessGame.board(), chessGame.turn()));
            confirmKingDead(chessGame);
        }
        if (command.isStatus()) {
            OutputView.printStatus(new PointDto(chessGame.calculatePoint()));
        }
    }

    private void confirmKingDead(ChessGame chessGame) {
        if (chessGame.isEnd()) {
            OutputView.printWinner(chessGame.winner());
        }
    }
}
