package wooteco.chess.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.TurnDao;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDto;

public class GameManagerService {
	private final BoardDao boardDao;
	private final TurnDao turnDao;

	public GameManagerService() {
		this.boardDao = new BoardDao();
		this.turnDao = new TurnDao();
	}

	public void move(Position targetPosition, Position destination) {
		Board board = new Board(boardDao.findAllPieces());
		Color turn = turnDao.findTurn();
		GameManager gameManager = new GameManager(board, turn);

		gameManager.move(targetPosition, destination);

		boardDao.editPieceByPosition(destination, boardDao.findPiece(targetPosition));
		boardDao.deletePieceByPosition(targetPosition);
		turnDao.editTurn(gameManager.getCurrentTurn());
	}

	public void resetGame() {
		boardDao.deleteAll();
		turnDao.editTurn(Color.WHITE);
		Board board = BoardFactory.create();
		List<PieceDto> pieces = board.getPieces().entrySet().stream()
			.map(x -> PieceDto.of(x.getKey(), x.getValue()))
			.collect(Collectors.toList());

		boardDao.addAllPieces(pieces);
	}

	public Board getBoard() {
		return new Board(boardDao.findAllPieces());
	}

	public Color getCurrentTurn() {
		return turnDao.findTurn();
	}

	public boolean isKingAlive() {
		Board board = new Board(boardDao.findAllPieces());
		Color turn = turnDao.findTurn();
		GameManager gameManager = new GameManager(board, turn);

		return gameManager.isKingAlive();
	}

	public Map<Color, Double> calculateEachScore() {
		Board board = new Board(boardDao.findAllPieces());
		Color turn = turnDao.findTurn();
		GameManager gameManager = new GameManager(board, turn);

		return gameManager.calculateEachScore();
	}
}
