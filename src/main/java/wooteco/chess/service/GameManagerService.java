package wooteco.chess.service;

import java.util.Map;

import wooteco.chess.dao.GameDao;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.GameManagerDto;

public class GameManagerService {
	private final GameDao gameDao;

	public GameManagerService() {
		this.gameDao = new GameDao();
	}

	public void move(Position targetPosition, Position destination) {
		GameManager gameManager = gameDao.findGame(1);
		gameManager.move(targetPosition, destination);

		gameDao.updateGame(new GameManagerDto(gameManager));
	}

	public void resetGame() {
		Board board = BoardFactory.create();
		GameManager gameManager = new GameManager(board, Color.WHITE);

		gameDao.updateGame(new GameManagerDto(gameManager));
	}

	public Board getBoard() {
		return gameDao.findGame(1).getBoard();
	}

	public Color getCurrentTurn() {
		return gameDao.findGame(1).getCurrentTurn();
	}

	public boolean isKingAlive() {
		GameManager game = gameDao.findGame(1);
		Board board = game.getBoard();
		Color currentTurn = game.getCurrentTurn();
		GameManager gameManager = new GameManager(board, currentTurn);

		return gameManager.isKingAlive();
	}

	public Map<Color, Double> calculateEachScore() {
		GameManager gameManager = new GameManager(gameDao.findGame(1).getBoard());

		return gameManager.calculateEachScore();
	}
}
