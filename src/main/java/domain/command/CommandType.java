package domain.command;

import domain.command.exceptions.CommandTypeException;

import java.util.Arrays;

public enum CommandType {
	START("start"),
	END("end"),
	STATUS("status"),
	MOVE("move [a-h][1-8] [a-h][1-8]");

	private final String regex;

	CommandType(final String regex) {
		this.regex = regex;
	}

	public static CommandType getInstance(String command) {
		return Arrays.stream(values())
				.filter(value -> command.matches(value.regex))
				.findFirst()
				.orElseThrow(() -> new CommandTypeException("올바르지 않은 명령어입니다."));
	}
}
