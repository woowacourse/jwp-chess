package chess.controller.menu;

import chess.controller.ChessController;
import chess.domain.board.strategy.BoardGenerationStrategy;
import chess.domain.board.strategy.WebBasicBoardStrategy;

public class Start implements Menu {

    private final BoardGenerationStrategy boardGenerator = new WebBasicBoardStrategy();

    @Override
    public void play(ChessController chessController) {
            chessController.start(boardGenerator);
    }
}
