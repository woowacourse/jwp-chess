package wooteco.chess.controller;

import wooteco.chess.domain.ChessManager;
import wooteco.chess.domain.position.Positions;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.PositionDto;
import wooteco.chess.view.ConsoleOutputView;
import wooteco.chess.view.OutputView;

import java.util.Arrays;
import java.util.List;

public class ConsoleController {
    private static OutputView outputView = new ConsoleOutputView();

    public static void start(ChessManager chessManager, String input) {
        chessManager.start();
        printBoard(new BoardDto(chessManager.getBoard()));
    }

    public static void end(ChessManager chessManager, String input) {
        chessManager.end();
    }

    public static void move(ChessManager chessManager, String input) {
        List<String> moveCommand = Arrays.asList(input.split(" "));
        chessManager.move(moveCommand.get(1), moveCommand.get(2));
        printBoard(new BoardDto(chessManager.getBoard()));

        chessManager.getWinner().ifPresent(outputView::printWinner);
    }

    public static void status(ChessManager chessManager, String input) {
        outputView.printStatus(chessManager.calculateScore(), chessManager.getCurrentTeam());
    }

    private static void printBoard(BoardDto boardDto) {
        PositionDto positionDto = new PositionDto(Positions.get());
        outputView.printBoard(positionDto.get(), boardDto.get());
    }
}
