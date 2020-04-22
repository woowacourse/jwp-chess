package wooteco.chess.domain.command;

import java.util.List;

import wooteco.chess.domain.ChessGame;

public class StartStrategy implements CommandStrategy {
	@Override
	public ChessGame execute(List<String> splitedInput, ChessGame chessGame) {
		return ChessGame.start();
	}
}
