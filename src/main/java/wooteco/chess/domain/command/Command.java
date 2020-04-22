package wooteco.chess.domain.command;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.ChessGame;

public enum Command {
	START(new StartStrategy()),
	MOVE(new MoveStrategy()),
	END((splittedInput, chessGame) -> {
		chessGame.end();
		return chessGame;
	});

	private CommandStrategy commandStrategy;

	Command(CommandStrategy commandStrategy) {
		this.commandStrategy = commandStrategy;
	}

	public static Command of(String input) {
		return Arrays.stream(values())
				.filter(command -> command.isMatch(input))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("일치하는 명령이 없습니다."));
	}

	public static ChessGame execute(String input, ChessGame chessGame) {
		List<String> commands = splitInputCommand(input);
		return Command.of(commands.get(0)).commandStrategy.execute(commands, chessGame);
	}

	private static List<String> splitInputCommand(String input) {
		return Arrays.asList(input.split(" "));
	}

	private boolean isMatch(String input) {
		return toString().equalsIgnoreCase(input);
	}
}
