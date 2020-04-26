package wooteco.chess.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import chess.dao.BoardDao;
import chess.dao.TurnDao;
import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.command.MoveCommand;
import chess.dto.BoardDto;
import chess.dto.CellManager;
import chess.dto.TurnDto;

@Service
public class ChessGameService {
	private BoardDao boardDAO = BoardDao.getInstance();
	private TurnDao turnDAO = TurnDao.getInstance();

	public Map<String, Object> loadBoard() throws SQLException {
		ChessBoard chessBoard = createBoardFromDb();
		return createModel(chessBoard);
	}

	public Map<String, Object> createNewChessGame() throws SQLException {
		ChessBoard chessBoard = new ChessBoard();

		turnDAO.deleteAll();
		boardDAO.saveBoard(BoardDto.from(chessBoard));
		turnDAO.saveTurn(TurnDto.from(chessBoard));

		return createModel(chessBoard);
	}

	public Map<String, Object> movePiece(String source, String target) throws SQLException {
		ChessBoard chessBoard = createBoardFromDb();
		chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

		this.boardDAO.deleteAll();
		this.boardDAO.saveBoard(BoardDto.from(chessBoard));
		this.turnDAO.updateTurn(TurnDto.from(chessBoard));

		return createModel(chessBoard);
	}

	public void endGame() throws SQLException {
		this.boardDAO.deleteAll();
		this.turnDAO.deleteAll();
	}

	public Map<String, Object> findWinner() throws SQLException {
		Map<String, Object> model = new HashMap<>();

		ChessBoard chessBoard = createBoardFromDb();

		GameResult gameResult = chessBoard.createGameResult();
		model.put("winner", gameResult.getWinner());
		model.put("loser", gameResult.getLoser());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

		endGame();
		return model;
	}

	public boolean isGameOver() throws SQLException {
		ChessBoard chessBoard = createBoardFromDb();
		return chessBoard.isGameOver();
	}

	public boolean isNotGameOver() throws SQLException {
		return !isGameOver();
	}

	private Map<String, Object> createModel(ChessBoard chessBoard) {
		Map<String, Object> model = new HashMap<>();
		GameResult gameResult = chessBoard.createGameResult();
		CellManager cellManager = new CellManager();

		model.put("cells", cellManager.createCells(chessBoard));
		model.put("currentTeam", chessBoard.getTeam().getName());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

		return model;
	}

	private ChessBoard createBoardFromDb() throws SQLException {
		BoardDto boardDto = boardDAO.findBoard();
		TurnDto turnDto = turnDAO.findTurn();
		return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
	}
}
