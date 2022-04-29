package chess.controller;

import chess.domain.ChessGame;
import chess.domain.MovingPosition;
import chess.utils.ScoreCalculator;
import chess.view.OutputView;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.BiConsumer;

import static chess.view.InputView.inputCommand;
import static chess.view.OutputView.printStartMessage;

public class ConsoleChessController {

    private static final Map<String, BiConsumer<StringTokenizer, ChessGame>> command = Map.of(
            "start", (stringTokenizer, chessGame) -> {
                chessGame.start();
                OutputView.printBoard(chessGame.getChessBoard());
            },
            "end", (stringTokenizer, chessGame) -> {
                chessGame.end();
                OutputView.printBoard(chessGame.getChessBoard());
            },
            "move", (stringTokenizer, chessGame) -> {
                chessGame.move(new MovingPosition(stringTokenizer.nextToken(), stringTokenizer.nextToken()));
                OutputView.printBoard(chessGame.getChessBoard());
            },
            "status", (stringTokenizer, chessGame) -> {
                OutputView.printScore(ScoreCalculator.computeScore(chessGame.getChessBoard()));
            }
    );

    public void run() {
        ChessGame chessGame = new ChessGame();
        printStartMessage();
        while (!chessGame.isFinished()) {
            playTurn(chessGame);
        }
        OutputView.printScore(ScoreCalculator.computeScore(chessGame.getChessBoard()));
    }

    private void playTurn(ChessGame chessGame) {
        StringTokenizer stringTokenizer = new StringTokenizer(inputCommand());
        String inputCommand = stringTokenizer.nextToken();

        if (!command.containsKey(inputCommand)) {
            throw new IllegalArgumentException("올바른 명령어를 입력해주세요");
        }

        command.get(inputCommand)
                .accept(stringTokenizer, chessGame);
    }


}
