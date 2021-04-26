package chess.domain.manager;

import chess.dao.dto.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.InitBoardInitializer;
import chess.domain.board.LoadBoardInitializer;
import chess.domain.converter.PiecesConverter;
import chess.domain.statistics.ChessGameStatistics;

import static chess.domain.converter.PiecesConverter.convertSquares;
import static chess.domain.piece.attribute.Color.WHITE;

public class ChessGameManagerFactory {
    private ChessGameManagerFactory() {
    }

    public static ChessGameManager createRunningGame(long id, String title) {
        return new RunningGameManager(id, InitBoardInitializer.getBoard(), title, WHITE);
    }

    public static ChessGameManager createEndGame(long id, String title, ChessGameStatistics chessGameStatistics, Board board) {
        return new EndChessGameManager(id, title, chessGameStatistics, board);
    }

    public static ChessGameManager createNotStartedGameManager(long id, String title) {
        return new NotStartedChessGameManager(id, title);
    }

    public static ChessGameManager loadingGame(ChessGame chessGame) {
        Board loadedBoard = LoadBoardInitializer.getBoard(convertSquares(chessGame.getPieces()));
        if (chessGame.isRunning()) {
            return new RunningGameManager(chessGame.getId(), loadedBoard, chessGame.getTitle(), chessGame.getNextTurn());
        }
        return createEndGame(chessGame.getId(), chessGame.getTitle(), ChessGameStatistics.createNotStartGameResult(),
                LoadBoardInitializer.getBoard(PiecesConverter.convertSquares(chessGame.getPieces())));
    }
}
