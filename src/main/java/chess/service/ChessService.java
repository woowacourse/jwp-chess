package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.GameId;
import chess.domain.game.score.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final GameDao gameDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public void initializeGame(GameId gameId) {
        gameDao.deleteGame(gameId);
        createGame(gameId);
    }

    public void createGame(GameId gameId) {
        gameDao.createGame(gameId);

        Board initializedBoard = Board.createInitializedBoard();
        for (Entry<Position, Piece> entry : initializedBoard.getValue().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();

            boardDao.createPiece(gameId, position, piece);
        }
    }

    public void movePiece(GameId gameId, Position from, Position to) {
        ChessGame chessGame = generateChessGame(gameId);
        chessGame.movePiece(from, to);

        updateGameTurn(gameId, chessGame);
        updatePiecePosition(gameId, from, to);
    }

    private void updatePiecePosition(GameId gameId, Position from, Position to) {
        boardDao.deletePiece(gameId, to);
        boardDao.updatePiecePosition(gameId, from, to);
    }

    private void updateGameTurn(GameId gameId, ChessGame chessGame) {
        if (chessGame.isWhiteTurn()) {
            gameDao.updateTurnToWhite(gameId);
            return;
        }

        gameDao.updateTurnToBlack(gameId);
    }

    public PieceColor getCurrentTurn(GameId gameId) {
        ChessGame chessGame = generateChessGame(gameId);
        if (chessGame.isWhiteTurn()) {
            return PieceColor.WHITE;
        }

        return PieceColor.BLACK;
    }

    public Score getScore(GameId gameId, PieceColor pieceColor) {
        ChessGame chessGame = generateChessGame(gameId);
        return chessGame.getStatus().getScoreByPieceColor(pieceColor);
    }

    public PieceColor getWinColor(GameId gameId) {
        ChessGame chessGame = generateChessGame(gameId);
        return chessGame.getWinColor();
    }

    private ChessGame generateChessGame(GameId gameId) {
        Board board = boardDao.getBoard(gameId);
        PieceColor currentTurn = gameDao.getCurrentTurn(gameId);
        return ChessGame.of(board, currentTurn);
    }

    public Board getBoard(GameId gameId) {
        return boardDao.getBoard(gameId);
    }

    @Override
    public String toString() {
        return "ChessService{" +
                "gameDao=" + gameDao +
                ", boardDao=" + boardDao +
                '}';
    }
}
