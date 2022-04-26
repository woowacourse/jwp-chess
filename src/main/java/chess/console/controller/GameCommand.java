package chess.console.controller;

import java.util.Arrays;
import java.util.function.BiConsumer;

public enum GameCommand {
    START("start", ConsoleChessController::start),
    END("end", ConsoleChessController::end),
    MOVE("move", ConsoleChessController::move),
    STATUS("status", ConsoleChessController::status);

    private final String commandLine;
    private final BiConsumer<ConsoleChessController, GameCommandRequest> requestBiConsumer;

    GameCommand(final String commandLine,
                final BiConsumer<ConsoleChessController, GameCommandRequest> requestBiConsumer) {
        this.commandLine = commandLine;
        this.requestBiConsumer = requestBiConsumer;
    }

    public static GameCommand findCommand(final String commandLine) {
        return Arrays.stream(values())
                .filter(gameStartCommand -> gameStartCommand.commandLine.equals(commandLine))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("잘못된 게임 커맨드입니다. %s", commandLine)));
    }

    public boolean isMove() {
        return this.equals(MOVE);
    }

    public void executeRequest(final ConsoleChessController controller, final GameCommandRequest request) {
        this.requestBiConsumer.accept(controller, request);
    }
}
