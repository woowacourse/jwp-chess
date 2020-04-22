package wooteco.chess.contoller;

import static wooteco.chess.view.InputView.*;
import static wooteco.chess.view.OutputView.*;

import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.Side;
import wooteco.chess.domain.command.Command;

public class ConsoleController {
	public void run() {
		printInitMessage();
		ChessGame chessGame = ChessGame.start();
		while (!chessGame.isEnd()) {
			printInitBoard(chessGame.getBoard());
			printScore(chessGame.status(Side.BLACK), chessGame.status(Side.WHITE));
			chessGame = Command.execute(inputCommand(), chessGame);
		}
	}
}
