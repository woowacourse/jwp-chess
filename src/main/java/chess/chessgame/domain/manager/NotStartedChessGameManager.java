package chess.chessgame.domain.manager;

import chess.chessgame.domain.board.Board;
import chess.chessgame.domain.board.InitBoardInitializer;
import chess.chessgame.domain.statistics.ChessGameStatistics;

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
