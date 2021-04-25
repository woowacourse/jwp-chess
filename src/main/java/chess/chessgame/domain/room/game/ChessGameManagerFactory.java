package chess.chessgame.domain.room.game;

import chess.chessgame.domain.room.game.board.Board;
import chess.chessgame.domain.room.game.board.InitBoardInitializer;
import chess.chessgame.domain.room.game.board.LoadBoardInitializer;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;
import chess.converter.PiecesConverter;
import chess.mysql.dao.dto.ChessGameDto;

import static chess.chessgame.domain.room.game.board.piece.attribute.Color.WHITE;
import static chess.converter.PiecesConverter.convertSquares;

public class ChessGameManagerFactory {
    private ChessGameManagerFactory() {
    }

    public static ChessGameManager createRunningGame(long id) {
        return new RunningGameManager(id, InitBoardInitializer.getBoard(), WHITE);
    }

    public static ChessGameManager createEndGame(long id, ChessGameStatistics chessGameStatistics, Board board) {
        return new EndChessGameManager(id, chessGameStatistics, board);
    }

    public static ChessGameManager createNotStartedGameManager(long id) {
        return new NotStartedChessGameManager(id);
    }

    public static ChessGameManager loadingGame(ChessGameDto chessGameDto) {
        Board loadedBoard = LoadBoardInitializer.getBoard(convertSquares(chessGameDto.getPieces()));
        if (chessGameDto.isRunning()) {
            return new RunningGameManager(chessGameDto.getId(), loadedBoard, Color.of(chessGameDto.getNextTurn()));
        }
        return createEndGame(chessGameDto.getId(), ChessGameStatistics.createNotStartGameResult(),
                LoadBoardInitializer.getBoard(PiecesConverter.convertSquares(chessGameDto.getPieces())));
    }
}
