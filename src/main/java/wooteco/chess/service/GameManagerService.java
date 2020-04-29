package wooteco.chess.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.GameDao;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.GameManagerDto;

@Service
public class GameManagerService {
	private final GameDao gameDao;

	public GameManagerService(GameDao gameDao) {
		this.gameDao = gameDao;
	}

	public void move(Position targetPosition, Position destination, int roomNo) {
		GameManager gameManager = gameDao.findGame(roomNo);
		gameManager.move(targetPosition, destination);

		gameDao.updateGame(new GameManagerDto(gameManager), roomNo);
	}

	public int newGame() {
		Board board = BoardFactory.create();
		GameManager gameManager = new GameManager(board, Color.WHITE);

		return gameDao.addGame(new GameManagerDto(gameManager));
	}

	public void deleteGame(int roomNo) {
		gameDao.deleteGame(roomNo);
	}

	public Board getBoard(int roomNo) {
		return gameDao.findGame(roomNo).getBoard();
	}

	public Color getCurrentTurn(int roomNo) {
		return gameDao.findGame(roomNo).getCurrentTurn();
	}

	public boolean isKingAlive(int roomNo) {
		GameManager game = gameDao.findGame(roomNo);
		Board board = game.getBoard();
		Color currentTurn = game.getCurrentTurn();
		GameManager gameManager = new GameManager(board, currentTurn);

		return gameManager.isKingAlive();
	}

	public Map<Color, Double> calculateEachScore(int roomNo) {
		GameManager gameManager = new GameManager(gameDao.findGame(roomNo).getBoard());

		return gameManager.calculateEachScore();
	}

	public List<String> getAllRoomNo() {
		return gameDao.findAllRoomNo();
	}
}
