package wooteco.chess.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;
import wooteco.chess.repository.GameDAO;
import wooteco.chess.view.dto.requestdto.PositionRequestDTO;
import wooteco.chess.view.dto.responsedto.BoardDTO;
import wooteco.chess.view.dto.responsedto.GameDTO;
import wooteco.chess.view.dto.responsedto.ScoreDTO;

@Service
public class GameService {
	private static final String NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE = "조건에 해당하는 요소가 없습니다.";
	private static final int DEFAULT_USER_ID = 1;

	private final GameDAO gameDAO;

	public GameService(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	public List<ScoreDTO> calculateScore() {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		Result status = game.status();
		return status.getStatus().entrySet().stream()
			.map(entry -> new ScoreDTO(entry.getKey().getTeam(), entry.getValue()))
			.collect(Collectors.toList());
	}

	public void changeState(String request) {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		game = game.changeState(request);
		gameDAO.update(game);
	}

	public List<BoardDTO> findAllPiecesOnBoard() {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		Board board = game.getBoard();

		return board.getPieces().entrySet().stream()
			.map(entry -> new BoardDTO(entry.getKey().toString(), entry.getValue().getSymbol()))
			.collect(Collectors.toList());
	}

	public void move(PositionRequestDTO positionRequestDTO) {
		Position from = Position.of(positionRequestDTO.getFrom());
		Position to = Position.of(positionRequestDTO.getTo());

		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		game = game.move(from, to);
		gameDAO.update(game);
	}

	public List<BoardDTO> findChangedPiecesOnBoard(PositionRequestDTO positionRequestDTO) {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		Position from = Position.of(positionRequestDTO.getFrom());
		Position to = Position.of(positionRequestDTO.getTo());
		Board board = game.getBoard();

		List<BoardDTO> result = new ArrayList<>();
		result.add(new BoardDTO(from.toString(), board.findSymbol(from)));
		result.add(new BoardDTO(to.toString(), board.findSymbol(to)));
		return result;
	}

	public GameDTO getCurrentState() {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		return GameDTO.of(game.getTurn(), game.getStateType());
	}

	public String getWinner() {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		return game.getWinner().name().toLowerCase();
	}

	public boolean isNotFinish() {
		Game game = gameDAO.findById(DEFAULT_USER_ID)
			.orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
		return game.isNotFinished();
	}
}