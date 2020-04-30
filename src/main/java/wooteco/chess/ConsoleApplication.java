package wooteco.chess;

import org.springframework.context.annotation.Bean;

import wooteco.chess.controller.console.ConsoleController;
import wooteco.chess.db.ConnectionLoader;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.state.Ready;

public class ConsoleApplication {
	public static void main(String[] args) {
		ConsoleController controller = new ConsoleController(new ChessGame(new Ready()));
		controller.run();
	}

	@Bean
	public ConnectionLoader connectionLoader() {
		return new ConnectionLoader();
	}
}
