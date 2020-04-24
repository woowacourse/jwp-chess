package wooteco.chess.controller.command;

import wooteco.chess.domain.ChessManager;
import wooteco.chess.domain.position.Positions;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.PositionDto;
import wooteco.chess.view.ConsoleOutputView;
import wooteco.chess.view.OutputView;

import java.util.Arrays;
import java.util.List;

public class CommandProcessor {
    private static OutputView outputView = new ConsoleOutputView();

    static void start(ChessManager chessManager, String input) {
        chessManager.start();
        printBoard(new BoardDto(chessManager.getBoard()));
    }

    static void end(ChessManager chessManager, String input) {
        chessManager.end();
    }

    static void move(ChessManager chessManager, String input) {
        List<String> moveCommand = Arrays.asList(input.split(" "));
        chessManager.move(moveCommand.get(1), moveCommand.get(2));
        printBoard(new BoardDto(chessManager.getBoard()));

        chessManager.getWinner().ifPresent(outputView::printWinner);
    }

    static void status(ChessManager chessManager, String input) {
        outputView.printStatus(chessManager.calculateScore(), chessManager.getCurrentTeam());
    }

    private static void printBoard(BoardDto boardDto) {
        PositionDto positionDto = new PositionDto(Positions.get());
        outputView.printBoard(positionDto.get(), boardDto.get());
    }
}
