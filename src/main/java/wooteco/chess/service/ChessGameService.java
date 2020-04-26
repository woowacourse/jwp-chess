package wooteco.chess.service;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import chess.dto.CellManager;
import chess.dto.TurnDto;
import org.springframework.stereotype.Service;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.TurnRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChessGameService {
	private BoardRepository boardRepository;
	private TurnRepository turnRepository;

	public ChessGameService(BoardRepository boardRepository, TurnRepository turnRepository) {
		this.boardRepository = boardRepository;
		this.turnRepository = turnRepository;
	}

	public Map<String, Object> loadBoard() throws SQLException {
		ChessBoard chessBoard = createBoardFromDb();
		return createModel(chessBoard);
	}

	public Map<String, Object> createNewChessGame() throws SQLException {
		ChessBoard chessBoard = new ChessBoard();
		Map<Position, Piece> board = chessBoard.getBoard();

		this.turnRepository.deleteAll();
		for (Position position : board.keySet()) {
			System.out.println(position.getName());
			System.out.println(board.get(position).getName());
			this.boardRepository.insert(position.getName(), board.get(position).getName());
		}
		this.turnRepository.update(TurnDto.from(chessBoard).getCurrentTeam());

		return createModel(chessBoard);
	}

	public Map<String, Object> movePiece(String source, String target) throws SQLException {
		ChessBoard chessBoard = createBoardFromDb();
		Map<Position, Piece> board = chessBoard.getBoard();
		chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

		this.boardRepository.deleteAll();
		for (Position position : board.keySet()) {
			this.boardRepository.insert(position.getName(), board.get(position).getName());
		}
		this.turnRepository.update(TurnDto.from(chessBoard).getCurrentTeam());

		return createModel(chessBoard);
	}

	public void endGame() throws SQLException {
		this.boardRepository.deleteAll();
		this.turnRepository.deleteAll();
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
		BoardDto boardDto = this.boardRepository.findFirst();
		TurnDto turnDto = this.turnRepository.findFirst();
		return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
	}
}
