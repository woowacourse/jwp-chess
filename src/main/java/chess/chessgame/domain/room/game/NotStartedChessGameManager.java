package chess.chessgame.domain.room.game;

import chess.chessgame.domain.room.game.board.Board;
import chess.chessgame.domain.room.game.board.InitBoardInitializer;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;

public class NotStartedChessGameManager extends NotRunningGameManager {
    public NotStartedChessGameManager(long id) {
        super(id);
    }

    @Override
    public boolean isNotEnd() {
        return true;
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public boolean isStart() {
        return false;
    }

    @Override
    public Board getBoard() {
        return InitBoardInitializer.getBoard();
    }

    @Override
    public ChessGameStatistics getStatistics() {
        throw new UnsupportedOperationException("게임이 진행중이지 않아 실행할 수 없습니다.");
    }
}
