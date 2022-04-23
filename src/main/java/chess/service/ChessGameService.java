package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessBoard;
import chess.domain.Color;
import chess.domain.Position;
import chess.domain.PromotionPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.state.ChessGameState;
import chess.domain.state.Turn;
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

	public long createNewChessGame() {
		long chessGameId = chessGameDao.createChessGame(Turn.WHITE_TURN);
		pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard(chessGameId));
		return chessGameId;
	}

	public Map<Position, Piece> findChessBoard(long chessGameId) {
		return findChessGameState(chessGameId).pieces();
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

	private ChessGameState findChessGameState(long chessGameId) {
		Turn currentTurn = findChessGameTurn(chessGameId);
		ChessBoard chessBoard = pieceDao.findChessBoardByChessGameId(chessGameId);
		return currentTurn.createGameTurn(chessBoard);
	}

	private Turn findChessGameTurn(long chessGameId) {
		return chessGameDao.findChessGame(chessGameId);
	}
}
