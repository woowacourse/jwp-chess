package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessBoard;
import chess.domain.Position;
import chess.domain.piece.PieceFactory;
import chess.domain.state.ChessGameState;
import chess.domain.state.Turn;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

	private final PieceDao pieceDao;
	private final ChessGameDao chessGameDao;

	public ChessGameService(PieceDao pieceDao, ChessGameDao chessGameDao) {
		this.pieceDao = pieceDao;
		this.chessGameDao = chessGameDao;
	}

	public long createNewChessGame() {
		long chessGameId = chessGameDao.createChessGame(Turn.WHITE_TURN);
		pieceDao.savePieces(PieceFactory.createNewChessBoard(chessGameId));
		return chessGameId;
	}

	public ChessBoard findChessBoard(long chessGameId) {
		return pieceDao.findChessBoardByChessGameId(chessGameId);
	}

	public void move(Position source, Position target) {
//		ChessGameState chessGameState = findGameTurn();
//		chessGameState.movePiece(source, target);
//
//		pieceDao.delete(target);
//		pieceDao.updatePiecePosition(source, target
//		turnDao.updateTurn(chessGameState.currentTurn(), chessGameState.nextTurn());
	}
}
