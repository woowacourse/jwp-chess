package chess.console.controller;

import chess.console.view.InputView;
import chess.console.view.OutputView;
import chess.model.File;
import chess.model.Rank;
import chess.model.board.Square;
import chess.model.game.ChessGame;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.service.dto.response.GameResultDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ConsoleChessController {

    private static final int FROM_INDEX = 0;
    private static final int TO_INDEX = 1;

    private final ChessGame game;

    public ConsoleChessController(final ChessGame game) {
        this.game = game;
    }

    public void run() {
        do {
            runUntilValid(this::executeByInput);
        } while (!game.isEnd());
    }

    private void runUntilValid(final Runnable runner) {
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

    public void start(final GameCommandRequest request) {
        OutputView.startGame();
        game.init();
        OutputView.printBoard(getAllPieceLetter(game));
    }

    public void move(final GameCommandRequest request) {
        List<String> body = request.getBody();
        Square from = Square.of(body.get(FROM_INDEX));
        Square to = Square.of(body.get(TO_INDEX));
        game.move(from, to);
        OutputView.printBoard(getAllPieceLetter(game));
        if (game.isEnd()) {
            status(request);
        }
    }

    public void status(final GameCommandRequest request) {
        GameResultDto gameResultDto = GameResultDto.of(game.getResult());
        OutputView.printWinner(gameResultDto);
        game.end();
    }

    public void end(final GameCommandRequest request) {
        game.end();
        OutputView.printEndMessage();
    }

    public List<List<String>> getAllPieceLetter(final ChessGame game) {
        return Rank.getRanksInBoardOrder().stream()
                .map(rank -> getPieceLetterInRank(game.getBoard(), rank))
                .collect(Collectors.toList());
    }

    private List<String> getPieceLetterInRank(final Map<Square, Piece> board, final Rank rank) {
        return Arrays.stream(File.values())
                .map(file -> board.get(Square.of(file, rank)))
                .map(PieceType::getLetterByColor)
                .collect(Collectors.toList());
    }
}
