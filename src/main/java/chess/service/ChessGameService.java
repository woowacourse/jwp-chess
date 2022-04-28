package chess.service;

import static chess.domain.state.Turn.END;
import static chess.domain.state.Turn.WHITE_TURN;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.Position;
import chess.domain.PromotionPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.state.ChessGameState;
import chess.domain.state.Turn;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChessGameService {

    private final PieceDao pieceDao;
    private final ChessGameDao chessGameDao;

    public ChessGameService(PieceDao pieceDao, ChessGameDao chessGameDao) {
        this.pieceDao = pieceDao;
        this.chessGameDao = chessGameDao;
    }

    public long createNewChessGame(String title, String password) {
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), title, password);
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);
        pieceDao.savePieces(savedChessGame.getId(), PieceFactory.createNewChessBoard());
        return savedChessGame.getId();
    }

    public List<ChessGame> findAllChessGame() {
        return chessGameDao.findAllChessGame();
    }

    public Map<Position, Piece> findChessBoard(long chessGameId, String password) {
        ChessGame chessGame = chessGameDao.findChessGame(chessGameId);
        chessGame.validatePassword(password);
        return pieceDao.findChessBoardByChessGameId(chessGameId).getPieces();
    }

    public void move(long chessGameId, Position source, Position target) {
        ChessGameState chessGameState = findChessGameState(chessGameId);
        chessGameState.movePiece(source, target);

        pieceDao.delete(chessGameId, target);
        pieceDao.updatePiecePosition(chessGameId, source, target);
        chessGameDao.changeChessGameTurn(chessGameId, chessGameState.nextTurn());
    }

    public void promotion(long chessGameId, PromotionPiece promotionPiece) {
        ChessGameState chessGameState = findChessGameState(chessGameId);
        Position position = chessGameState.promotion(promotionPiece);

        pieceDao.updatePieceRule(chessGameId, position, promotionPiece.pieceRule());
        chessGameDao.changeChessGameTurn(chessGameId, chessGameState.nextTurn());
    }

    public void endChessGame(long chessGameId) {
        ChessGame chessGame = chessGameDao.findChessGame(chessGameId);
        chessGame.validateRunningGame();

        chessGameDao.changeChessGameTurn(chessGameId, END);
    }

    public Map<Color, Double> currentScore(long chessGameId) {
        ChessGameState chessGameState = findChessGameState(chessGameId);
        return chessGameState.currentScore();
    }

    public boolean isEndGame(long chessGameId) {
        Turn currentTurn = findChessGameTurn(chessGameId);
        return currentTurn.isEnd();
    }

    public Color winner(long chessGameId) {
        ChessBoard chessBoard = pieceDao.findChessBoardByChessGameId(chessGameId);
        return chessBoard.winner();
    }

    public void deleteChessGame(long chessGameId, String password){
        ChessGame chessGame = chessGameDao.findChessGame(chessGameId);
        chessGame.validatePassword(password);
        chessGame.validateEndGame();

        chessGameDao.deleteChessGame(chessGame);
    }

    private ChessGameState findChessGameState(long chessGameId) {
        Turn currentTurn = findChessGameTurn(chessGameId);
        ChessBoard chessBoard = pieceDao.findChessBoardByChessGameId(chessGameId);
        return currentTurn.createGameTurn(chessBoard);
    }

    private Turn findChessGameTurn(long chessGameId) {
        return Turn.valueOf(chessGameDao.findChessGame(chessGameId).getTurn());
    }
}
