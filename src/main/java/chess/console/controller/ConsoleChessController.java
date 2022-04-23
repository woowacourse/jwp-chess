package chess.console.controller;

import chess.console.view.InputView;
import chess.console.view.OutputView;
import chess.model.ChessGame;
import chess.model.File;
import chess.model.GameCommand;
import chess.model.Rank;
import chess.model.board.Board;
import chess.model.board.Square;
import chess.model.piece.PieceType;
import chess.service.ChessService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ConsoleChessController {

    private static final int FROM_INDEX = 0;
    private static final int TO_INDEX = 1;

    private final ChessGame game;

    public ConsoleChessController(ChessGame game) {
        this.game = game;
    }

    public void run() {
        do {
            runUntilValid(this::executeByInput);
        } while (game.isPlaying());
    }

    private void runUntilValid(Runnable runner) {
        boolean runSuccess;
        do {
            runSuccess = tryRun(runner);
        } while (!runSuccess);
    }

    private boolean tryRun(Runnable runner) {
        try {
            runner.run();
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            OutputView.printException(e);
            return false;
        }
    }

    private void executeByInput() {
        GameCommandRequest request = GameCommandRequest.of(InputView.inputCommandRequest());
        GameCommand gameCommand = request.getGameCommand();
        gameCommand.executeRequest(this, request);
    }

    public void start(GameCommandRequest request) {
        OutputView.startGame();
        game.init();
        OutputView.printBoard(getAllPieceLetter(game));
    }

    public void move(GameCommandRequest request) {
        List<String> body = request.getBody();
        Square from = Square.of(body.get(FROM_INDEX));
        Square to = Square.of(body.get(TO_INDEX));
        game.move(from, to);
        OutputView.printBoard(getAllPieceLetter(game));
    }

    public void status(GameCommandRequest request) {
        OutputView.printWinner(game.getResult());
        this.end(request);
    }

    public void end(GameCommandRequest request) {
        game.end();
        OutputView.printEndMessage();
    }

    public List<List<String>> getAllPieceLetter(ChessGame game) {
        return Rank.getRanksInBoardOrder().stream()
                .map(rank -> getPieceLetterInRank(game.getBoard(), rank))
                .collect(Collectors.toList());
    }

    private List<String> getPieceLetterInRank(Board board, Rank rank) {
        return Arrays.stream(File.values())
                .map(file -> board.findPieceBySquare(Square.of(file, rank)))
                .map(PieceType::getLetterByColor)
                .collect(Collectors.toList());
    }
}
