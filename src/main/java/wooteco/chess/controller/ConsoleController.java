package wooteco.chess.controller;

import wooteco.chess.ChessGame;
import wooteco.chess.domain.position.Position;
import wooteco.chess.view.InputView;
import wooteco.chess.view.OutputView;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Consumer;

public class ConsoleController {
	private ChessGame game;
	private Map<String, Consumer<StringTokenizer>> commands;

	public ConsoleController(ChessGame game) {
		this.game = game;
		this.commands = new HashMap<String, Consumer<StringTokenizer>>() {{
			put("start", tokenizer -> {
				game.start();
				OutputView.printGameStart();
				OutputView.printBoard(game.board());
			});
			put("end", tokenizer -> game.end());
			put("move", tokenizer -> {
				game.move(Position.of(tokenizer.nextToken()),
					Position.of(tokenizer.nextToken()));
				OutputView.printBoard(game.board());
			});
			put("status", tokenizer -> OutputView.printStatus(game.status()));
		}};
	}

	public void command(String input) {
		StringTokenizer tokenizer = new StringTokenizer(input, " ");
		commands.get(tokenizer.nextToken()).accept(tokenizer);
	}

	public void run() {
		OutputView.printGameStart();
		while (!game.isEnd()) {
			command(InputView.inputRequest());
		}
	}
}

