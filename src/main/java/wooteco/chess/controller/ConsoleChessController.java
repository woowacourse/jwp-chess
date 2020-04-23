package wooteco.chess.controller;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.State;
import wooteco.chess.view.InputView;
import wooteco.chess.view.OutputView;

public class ConsoleChessController {
    private State state = State.BEFORE_START;

    public void run() {
        OutputView.printGameStartInstruction();
        Board board = new Board();
        setInitialStatus();

        while (state == State.RUNNING && board.isBothKingAlive()) {
            OutputView.printChessBoard(board);
            processSingleTurn(board);
        }
        if (!board.isBothKingAlive()) {
            OutputView.printWinner(board.getWinner());
        }
    }

    private void setInitialStatus() {
        try {
            state = State.of(InputView.inputGameStartOrEnd());
        } catch (Exception e) {
            OutputView.printErrorMessage(e.getMessage());
            setInitialStatus();
        }
    }

    private void processSingleTurn(Board board) {
        try {
            String decision = InputView.inputInstruction();
            state = Command.of(decision).execute(board, decision);
        } catch (Exception e) {
            OutputView.printErrorMessage(e.getMessage());
        }
    }
}
