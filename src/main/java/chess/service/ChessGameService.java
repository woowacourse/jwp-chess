package chess.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import chess.dao.BoardDAO;
import chess.dao.TurnDAO;
import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.board.PositionFactory;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDTO;
import chess.dto.Cell;
import chess.dto.TurnDTO;

public class ChessGameService {
	private static final char MIN_Y_POINT = '1';
	private static final char MAX_Y_POINT = '8';
	private static final char MIN_X_POINT = 'a';
	private static final char MAX_X_POINT = 'h';

	private BoardDAO boardDAO = BoardDAO.getInstance();
	private TurnDAO turnDAO = TurnDAO.getInstance();
	private ChessBoard chessBoard;
	private GameResult gameResult;

	public ChessGameService() throws SQLException {
		try {
			BoardDTO boardDto = boardDAO.findBoard();
			TurnDTO turnDto = turnDAO.findTurn();
			this.chessBoard = new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
		} catch (NoSuchElementException e) {
			this.chessBoard = new ChessBoard();
			turnDAO.saveTurn(TurnDTO.from(this.chessBoard));
		}

		this.gameResult = this.chessBoard.createGameResult();
	}

	public void setNewChessGame() {
		this.chessBoard = new ChessBoard();
		this.gameResult = this.chessBoard.createGameResult();
	}

	public List<Cell> getCells() {
		List<Cell> cells = new ArrayList<>();

		Map<Position, Piece> boardData = this.chessBoard.getBoard();

		for (char yPoint = MAX_Y_POINT; yPoint >= MIN_Y_POINT; yPoint--) {
			for (char xPoint = MIN_X_POINT; xPoint <= MAX_X_POINT; xPoint++) {
				Position position = PositionFactory.of(xPoint, yPoint);
				Piece piece = boardData.get(position);

				cells.add(Cell.from(position, piece));
			}
		}

		return cells;
	}

	public String getCurrentTeam() {
		return this.chessBoard.getTeam().getName();
	}

	public Double getBlackPieceScore() {
		return this.gameResult.getAliveBlackPieceScoreSum();
	}

	public Double getWhitePieceScore() {
		return this.gameResult.getAliveWhitePieceScoreSum();
	}

	public void movePiece(String source, String target) {
		this.chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));
		this.gameResult = this.chessBoard.createGameResult();
	}

	public boolean isGameOver() {
		return this.chessBoard.isGameOver();
	}

	public String getWinner() {
		return gameResult.findWinner();
	}

	public String getLoser() {
		return gameResult.findLoser();
	}

	public void endGame() throws SQLException {
		this.boardDAO.deletePreviousBoard();
		this.turnDAO.deletePreviousTurn();
		setNewChessGame();
	}

	public void proceedGame() throws SQLException {
		this.boardDAO.deletePreviousBoard();
		this.boardDAO.saveBoard(BoardDTO.from(this.chessBoard));
		this.turnDAO.updateTurn(turnDAO.findTurn());
	}
}
