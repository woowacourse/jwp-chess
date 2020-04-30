package wooteco.chess.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import chess.dto.TurnDto;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.TurnEntity;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.TurnRepository;

@Service
public class ChessGameService {
	private BoardRepository boardRepository;
	private TurnRepository turnRepository;

	public ChessGameService(BoardRepository boardRepository, TurnRepository turnRepository) {
		this.boardRepository = boardRepository;
		this.turnRepository = turnRepository;
	}

	public ChessBoard loadBoard() {
		return createBoardFromDb();
	}

	public ChessBoard createNewChessGame() {
		ChessBoard chessBoard = new ChessBoard();
		Map<Position, Piece> board = chessBoard.getBoard();

		this.turnRepository.deleteAll();
		for (Position position : board.keySet()) {
			this.boardRepository.save(
				new BoardEntity(position.getName(), board.get(position).getName())
			);
		}
		String currentTeam = TurnDto.from(chessBoard).getCurrentTeam();
		this.turnRepository.save(new TurnEntity(currentTeam));

		return chessBoard;
	}

	public ChessBoard movePiece(String source, String target) {
		ChessBoard chessBoard = createBoardFromDb();
		Map<Position, Piece> board = chessBoard.getBoard();
		chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

		this.boardRepository.deleteAll();
		for (Position position : board.keySet()) {
			this.boardRepository.save(
				new BoardEntity(position.getName(), board.get(position).getName())
			);
		}

		TurnEntity first = this.turnRepository.findFirst();

		this.turnRepository.save(
			new TurnEntity(
				first.getId(), TurnDto.from(chessBoard).getCurrentTeam()
			)
		);

		return chessBoard;
	}

	private void endGame() {
		this.boardRepository.deleteAll();
		this.turnRepository.deleteAll();
	}

	public GameResult findWinner() {
		ChessBoard chessBoard = createBoardFromDb();
		GameResult gameResult = chessBoard.createGameResult();
		endGame();
		return gameResult;
	}

	public boolean isGameOver() {
		ChessBoard chessBoard = createBoardFromDb();
		return chessBoard.isGameOver();
	}

	public boolean isNotGameOver() {
		return !isGameOver();
	}

	private ChessBoard createBoardFromDb() {
		List<BoardEntity> boardEntities = this.boardRepository.findAll();
		if (boardEntities.isEmpty()) {
			throw new NoSuchElementException("테이블의 행이 비었습니다!!");
		}
		BoardDto boardDto = new BoardDto(boardEntities);

		TurnDto turnDto = TurnDto.from(this.turnRepository.findFirst().getTeamName());

		return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
	}
}
