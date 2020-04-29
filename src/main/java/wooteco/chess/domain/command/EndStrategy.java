package wooteco.chess.domain.command;

import java.util.List;

import wooteco.chess.domain.ChessGame;

public class EndStrategy implements CommandStrategy {
	@Override
	public ChessGame execute(List<String> splitedInput, ChessGame chessGame) {
		chessGame.end();
		return chessGame;
	}
}
