package chess.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.Color;
import chess.domain.Result;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.chessgame.ChessGame;
import chess.exception.UserInputException;
import chess.repository.ChessGameRepository;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.web.dto.ResultDto;
import chess.web.dto.RoomResponseDto;

@Service
@Transactional(readOnly = true)
public class ChessGameService {

	private static final String NOT_EXIST_ROOM = "유효하지 않은 체스방 주소입니다.";

	private final ChessGameRepository chessGameRepository;

	public ChessGameService(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	@Transactional
	public ChessGame create(String name, String password) {
		validateDuplicateName(name);
		Board board = new Board(new RegularRuleSetup());
		return chessGameRepository.save(ChessGame.create(name, password, board));
	}

	private void validateDuplicateName(String name) {
		chessGameRepository.findByName(name)
			.ifPresent(game -> {
				throw new UserInputException("해당 이름의 방이 이미 존재합니다.");
			});
	}

	public ChessGame getChessGameById(int gameId) {
		return chessGameRepository.findById(gameId)
			.orElseThrow(() -> new UserInputException(NOT_EXIST_ROOM));
	}

	@Transactional
	public void delete(int gameId, String password) {
		ChessGame chessGame = getChessGameById(gameId);
		if (!chessGame.isRightPassword(password)) {
			throw new UserInputException("유효하지 않은 비밀번호입니다.");
		}
		if (!chessGame.isEnd()) {
			throw new UserInputException("게임이 끝나지 않아 삭제할 수 없습니다.");
		}
		chessGameRepository.removeById(gameId);
	}

	public List<RoomResponseDto> findAllResponseDto() {
		return chessGameRepository.findAll().stream()
			.map(game -> new RoomResponseDto(game.getId(), game.getName(), game.isEnd()))
			.collect(Collectors.toList());
	}

	@Transactional
	public int startNewGame(int gameId) {
		Board board = new Board(new RegularRuleSetup());
		return chessGameRepository.updateBoard(gameId, board);
	}

	public BoardDto getBoardDtoByBoardId(int boardId) {
		Board board = chessGameRepository.findBoardByBoardId(boardId);
		return new BoardDto(boardId, board);
	}

	public BoardDto getBoardDtoByGameId(int gameId) {
		Board board = chessGameRepository.getBoardById(gameId);
		board.loadTurn(chessGameRepository.getTurnById(gameId));
		return getBoardDtoByBoardId(chessGameRepository.getBoardIdById(gameId));
	}

	public ResultDto getResult(int boardId) {
		Board board = chessGameRepository.findBoardByBoardId(boardId);
		int whiteScore = (int) board.calculateScore(Color.WHITE);
		int blackScore = (int) board.calculateScore(Color.BLACK);
		return new ResultDto(whiteScore, blackScore, board.gameResult());
	}

	@Transactional
	public void move(int boardId, CommendDto commendDto) {
		String source = commendDto.getSource();
		String target = commendDto.getTarget();
		Board board = chessGameRepository.findBoardByBoardId(boardId);
		board.move(source, target);

		chessGameRepository.updatePiece(boardId, source, target);;
		chessGameRepository.updateTurn(boardId, board);
	}

	public ResultDto getFinalResult(int boardId) {
		Board board = chessGameRepository.findBoardByBoardId(boardId);
		return new ResultDto(
			(int) board.calculateScore(Color.WHITE),
			(int) board.calculateScore(Color.BLACK),
			Map.of(Result.WIN, board.getWinnerColor())
		);
	}
}
