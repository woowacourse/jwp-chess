package chess.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.Color;
import chess.domain.Result;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.exception.IllegalChessRuleException;
import chess.repository.BoardRepository;
import chess.repository.PieceRepository;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.domain.GameState;
import chess.web.dto.PieceDto;
import chess.web.dto.ResultDto;

@Service
@Transactional(readOnly = true)
public class GameService {

	private final PieceRepository pieceRepository;
	private final BoardRepository boardRepository;

	public GameService(PieceRepository pieceRepository, BoardRepository boardRepository) {
		this.pieceRepository = pieceRepository;
		this.boardRepository = boardRepository;
	}

	@Transactional
	public BoardDto startNewGame(int roomId) {
		Board board = new Board(new RegularRuleSetup());

		boardRepository.deleteByRoom(roomId);

		int boardId = boardRepository.save(roomId, GameState.from(board));
		pieceRepository.saveAll(boardId, board.getPieces());
		return gameStateAndPieces(boardId);
	}

	@Transactional
	public void move(int boardId, CommendDto commendDto) {
		String source = commendDto.getSource();
		String target = commendDto.getTarget();
		Board board = loadBoard(boardId);
		board.move(source, target);

		PieceDto pieceDto = pieceRepository.findOne(boardId, source)
			.orElseThrow(() -> new IllegalChessRuleException("피스가 존재하지 않습니다."));
		pieceRepository.deleteOne(boardId, source);
		updateMovedPieceToTarget(boardId, target, pieceDto);
		boardRepository.updateTurn(boardId, GameState.from(board));
	}

	private void updateMovedPieceToTarget(int boardId, String target, PieceDto pickedPieceDto) {
		pieceRepository.findOne(boardId, target)
			.ifPresentOrElse(
				pieceDto -> pieceRepository.updateOne(boardId, target, pickedPieceDto),
				() -> pieceRepository.save(boardId, target, pickedPieceDto)
			);
	}

	public BoardDto loadGame(int roomId) {
		int boardId = boardRepository.getBoardIdByRoom(roomId);
		Board board = loadBoard(boardId);
		board.loadTurn(boardRepository.getTurn(boardId));
		return gameStateAndPieces(boardId);
	}

	public BoardDto gameStateAndPieces(int boardId) {
		Board board = loadBoard(boardId);
		return new BoardDto(boardId, board);
	}

	public ResultDto gameResult(int boardId) {
		Board board = loadBoard(boardId);
		int whiteScore = (int) board.calculateScore(Color.WHITE);
		int blackScore = (int) board.calculateScore(Color.BLACK);
		return new ResultDto(whiteScore, blackScore, board.gameResult());
	}

	private Board loadBoard(int boardId) {
		Map<Position, Piece> pieces = new HashMap<>();
		for (PieceDto pieceDto : pieceRepository.findAll(boardId)) {
			pieces.put(Position.of(pieceDto.getPosition()), PieceFactory.build(pieceDto));
		}
		Board board = new Board(() -> pieces);
		board.loadTurn(boardRepository.getTurn(boardId));
		return board;
	}

	public ResultDto gameFinalResult(int boardId) {
		Board board = loadBoard(boardId);
		return new ResultDto(
			(int) board.calculateScore(Color.WHITE),
			(int) board.calculateScore(Color.BLACK),
			Map.of(Result.WIN, board.getWinnerColor())
		);
	}
}
