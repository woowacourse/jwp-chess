package wooteco.chess.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.BoardDAO;
import wooteco.chess.dao.TurnDAO;
import wooteco.chess.domain.Result;
import wooteco.chess.domain.Status;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.factory.BoardFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDTO;

@Service
public class ChessService {
	private static final boolean FIRST_TURN = true;

	private final BoardDAO boardDAO;
	private final TurnDAO turnDAO;

	public ChessService(final BoardDAO boardDAO, final TurnDAO turnDAO) {
		this.boardDAO = boardDAO;
		this.turnDAO = turnDAO;
	}

	public Board move(Position startPosition, Position targetPosition) {
		Board board = find();
		Piece startPiece = board.findByPosition(startPosition);
		board.move(startPosition, targetPosition);
		boardDAO.update(targetPosition, startPiece.getName());
		boardDAO.update(startPosition, ".");
		turnDAO.changeTurn(board.isWhiteTurn());
		return board;
	}

	public Board find() {
		List<PieceDTO> pieceDTOS = boardDAO.findAll();
		Turn turn;
		try {
			turn = turnDAO.find();
		} catch (NoSuchElementException e) {
			turn = new Turn(FIRST_TURN);
			turnDAO.addTurn(FIRST_TURN);
		}
		if (pieceDTOS.isEmpty()) {
			return createBoard(BoardFactory.createBoard());
		}
		return BoardFactory.createBoard(pieceDTOS, turn);
	}

	private Board createBoard(Board board) {
		List<Piece> pieces = board.findAll();
		for (Piece piece : pieces) {
			boardDAO.addPiece(PieceDTO.from(piece));
		}
		return board;
	}

	public Board restart() {
		boardDAO.removeAll();
		turnDAO.removeAll();
		return find();
	}

	public boolean isEnd() {
		Board board = find();
		return !board.isLiveBothKing();
	}

	public boolean isWinWhiteTeam() {
		Board board = find();
		return board.isLiveKing(Team.WHITE);
	}

	public Result status() {
		Board board = find();
		Status status = board.createStatus();
		return status.getResult();
	}
}
