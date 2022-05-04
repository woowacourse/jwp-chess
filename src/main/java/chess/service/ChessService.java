package chess.service;

import chess.db.ChessGameDao;
import chess.db.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameEntity;
import chess.domain.game.GameResult;
import chess.domain.game.GameTurn;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.SavedBoardGenerator;
import chess.domain.position.Movement;
import chess.domain.position.Square;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(final ChessGameDao chessGameDao, final PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public List<GameEntity> loadGameLists() {
        return chessGameDao.findAllGames();
    }

    public void createGame(String gameID, String gamePW) {
        ChessGame chessGame = loadNewChessGame();
        saveNewGame(gameID, gamePW, chessGame);
        loadPieces(gameID);
    }

    public void checkAndDeleteGame(String gameID, String inputPW) {
        if (chessGameDao.checkPassword(gameID, inputPW)) {
            chessGameDao.delete(gameID);
            pieceDao.deleteAll(gameID);
        }
    }

    public GameTurn getTurn(String gameCode) {
        String gameID = findGameID(gameCode);
        return GameTurn.find(chessGameDao.findTurnByID(gameID));
    }

    public boolean isFinished(String gameCode) {
        GameTurn gameTurn = getTurn(gameCode);
        return GameTurn.FINISHED.equals(gameTurn);
    }

    public ChessGame loadSavedChessGame(String gameCode) {
        String gameID = findGameID(gameCode);
        return new ChessGame(new SavedBoardGenerator(pieceDao.findByGameID(gameID)), getTurn(gameCode));
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    public void saveNewGame(String gameID, String gamePW, ChessGame chessGame) {
        String gameCode = "hash" + gameID + gamePW + "val";
        chessGameDao.save(gameID, gamePW, gameCode, chessGame);
        updateTurn(gameCode, chessGame);
    }

    public void loadPieces(String gameID) {
        pieceDao.deleteAll(gameID);
        pieceDao.save(gameID);
    }

    public GameResult getGameResult(String gameCode) {
        String gameID = findGameID(gameCode);
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameID(gameID)));
        return new GameResult(board);
    }

    public String findGameID(String gameCode) {
        return chessGameDao.findIDByCode(gameCode);
    }

    public void startGame(String gameCode, ChessGame chessGame) {
        chessGame.startGame();
        updateTurn(gameCode, chessGame);
    }

    public void move(String gameCode, ChessGame chessGame, String source, String target) {
        chessGame.move(new Square(source), new Square(target));
        movePiece(gameCode, source, target);
        updateTurn(gameCode, chessGame);
        promoteIfAvailable(chessGame, gameCode, target);
    }

    public void movePiece(String gameCode, String source, String target) {
        String gameID = findGameID(gameCode);
        pieceDao.deleteByPosition(new Square(target), gameID);
        pieceDao.updatePosition(new Square(source), new Square(target), gameID);
        pieceDao.insertNone(gameID, new Square(source));
    }

    public void updateTurn(String gameCode, ChessGame chessGame) {
        String gameID = findGameID(gameCode);
        chessGameDao.updateTurn(gameID, chessGame);
    }

    public void promoteIfAvailable(ChessGame chessGame, String gameCode, String target) {
        Square targetSquare = new Square(target);
        if (chessGame.isPromotionAvailable(targetSquare)) {
            chessGame.doPromotion(targetSquare);
            promotePawnToQueen(gameCode, target);
        }
    }

    private void promotePawnToQueen(String gameCode, String target) {
        String gameID = findGameID(gameCode);
        pieceDao.promotePiece(gameID, target, "QUEEN");
    }

    public Map<String, String> getEmojis(String gameCode) {
        String gameID = findGameID(gameCode);
        ChessGame chessGame = loadSavedChessGame(gameCode);
        startGame(gameCode, chessGame);
        loadPieces(gameID);
        return chessGame.getEmojis();
    }

    public void doCastling(String gameCode, ChessGame chessGame, String source, String target) {
        chessGame.doCastling(new Square(source), new Square(target));

        Square sourceSquare = new Square(source);
        Square targetSquare = new Square(target);

        boolean isQueenSide = sourceSquare.isPlacedOnRightSideOf(targetSquare);

        String movedSource = sourceSquare.add(sourceCastlingMovement(isQueenSide)).getName();
        String movedTarget = targetSquare.add(targetCastlingMovement(isQueenSide)).getName();

        movePiece(gameCode, source, movedSource);
        movePiece(gameCode, target, movedTarget);
        updateTurn(gameCode, chessGame);
    }

    private Movement sourceCastlingMovement(boolean isQueenSide) {
        if (isQueenSide) {
            return new Movement(-2, 0);
        }
        return new Movement(2, 0);
    }

    private Movement targetCastlingMovement(boolean isQueenSide) {
        if (isQueenSide) {
            return new Movement(3, 0);
        }
        return new Movement(-2, 0);
    }
}
