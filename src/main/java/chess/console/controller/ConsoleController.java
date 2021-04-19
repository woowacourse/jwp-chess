package chess.console.controller;

import chess.console.controller.dto.request.CommandRequestDto;
import chess.console.view.InputView;
import chess.console.view.OutputView;
import chess.domain.game.ChessGame;
import chess.web.controller.dto.response.GameStatusResponseDto;

public class ConsoleController {

    private static final String START_COMMAND_INPUT = "start";
    private static final String MOVE_COMMAND_INPUT = "move";
    private static final String STATUS_COMMAND_INPUT = "status";
    private static final String END_COMMAND_INPUT = "end";
    private static final String GAME_NOT_STARTED_ERROR_MESSAGE = "게임을 먼저 시작해야 합니다.";

    private ChessGame chessGame;

    public void run() {
        OutputView.printGameStartMessage();
        boolean isGameEnd = false;
        while (!isGameEnd) {
            CommandRequestDto commandRequestDto = InputView.getCommandRequest();
            GameStatus gameStatus = handleCommandAndGetGameStatus(commandRequestDto);
            isGameEnd = gameStatus.isGameEnd();
        }
    }

    private GameStatus handleCommandAndGetGameStatus(CommandRequestDto commandRequestDto) {
        if (START_COMMAND_INPUT.equals(commandRequestDto.getCommandInput())) {
            return start();
        }
        if (MOVE_COMMAND_INPUT.equals(commandRequestDto.getCommandInput())) {
            return move(commandRequestDto);
        }
        if (STATUS_COMMAND_INPUT.equals(commandRequestDto.getCommandInput())) {
            return status();
        }
        if (END_COMMAND_INPUT.equals(commandRequestDto.getCommandInput())) {
            return new GameStatus(true);
        }
        throw new IllegalArgumentException("유효하지 않은 명령어 입니다.");
    }

    private GameStatus start() {
        chessGame = new ChessGame();
        printGameStatus();
        return new GameStatus(false);
    }

    private void printGameStatus() {
        GameStatusResponseDto gameStatusResponseDto = new GameStatusResponseDto(chessGame);
        OutputView.printGameStatus(gameStatusResponseDto);
    }

    private GameStatus move(CommandRequestDto commandRequestDto) {
        if (chessGame == null) {
            System.out.println(GAME_NOT_STARTED_ERROR_MESSAGE);
            return new GameStatus(false);
        }
        String startPositionInput = commandRequestDto.getStartPositionInput();
        String destinationInput = commandRequestDto.getDestinationInput();
        GameStatusResponseDto gameStatusResponseDto = movePiece(startPositionInput, destinationInput);
        printGameStatus();
        return new GameStatus(gameStatusResponseDto.isKingDead());
    }

    private GameStatusResponseDto movePiece(String startPositionInput, String destinationInput) {
        try {
            chessGame.movePiece(startPositionInput, destinationInput);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new GameStatusResponseDto(chessGame);
    }

    private GameStatus status() {
        if (chessGame == null) {
            System.out.println(GAME_NOT_STARTED_ERROR_MESSAGE);
            return new GameStatus(false);
        }
        GameStatusResponseDto gameStatusResponseDto = new GameStatusResponseDto(chessGame);
        OutputView.printScores(gameStatusResponseDto);
        return new GameStatus(false);
    }
}
