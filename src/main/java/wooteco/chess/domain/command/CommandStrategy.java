package wooteco.chess.domain.command;

import java.util.List;

import wooteco.chess.domain.ChessGame;

public interface CommandStrategy {
	ChessGame execute(List<String> splittedInput, ChessGame chessGame);
}
