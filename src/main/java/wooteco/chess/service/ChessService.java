package wooteco.chess.service;

import static java.util.stream.Collectors.*;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.dao.BoardDAO;
import wooteco.chess.dao.TurnInfoDAO;
import wooteco.chess.domain.Status;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

public class ChessService {
	private final BoardDAO boardDAO;
	private final TurnInfoDAO turnInfoDAO;

	public ChessService(BoardDAO boardDAO, TurnInfoDAO turnInfoDAO) {
		this.boardDAO = boardDAO;
		this.turnInfoDAO = turnInfoDAO;
	}

	public void initialize(String gameId) {
		if (boardDAO.hasNotGameIn(gameId)) {
			boardDAO.addPieces(gameId, BoardFactory.toList());
			turnInfoDAO.initialize(gameId, Team.WHITE);
		}
	}

	public void move(String gameId, Position from, Position to) {
		Board board = Board.of(boardDAO.findAll(gameId));
		board.verifyMove(from, to, turnInfoDAO.findCurrent(gameId));

		boardDAO.update(gameId, from, to);
		turnInfoDAO.updateNext(gameId);
	}

	public Map<String, String> getBoard(String gameId) {
		return boardDAO.findAll(gameId)
			.stream()
			.collect(toMap(piece -> piece.getPosition().getName(), Piece::getSymbol));
	}

	public Map<String, String> getResult(String gameId) {
		Map<String, String> result = new HashMap<>();
		Status status = Status.of(boardDAO.findAll(gameId));

		String whiteScore = String.valueOf(status.toMap().get(Team.WHITE));
		String blackScore = String.valueOf(status.toMap().get(Team.BLACK));
		String winner = status.getWinner().name();

		result.put("whiteScore", whiteScore);
		result.put("blackScore", blackScore);
		result.put("result", winner);

		return result;
	}
}
