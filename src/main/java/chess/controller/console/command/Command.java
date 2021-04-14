package chess.controller.console.command;

import chess.domain.manager.ChessGameManager;
import chess.service.ChessServiceImpl;

@FunctionalInterface
public interface Command {
    ChessGameManager execute(ChessServiceImpl chessServiceImpl, long gameId);
}
