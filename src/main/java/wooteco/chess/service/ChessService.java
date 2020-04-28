package wooteco.chess.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.Result;
import wooteco.chess.domain.Status;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.factory.BoardFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.TurnEntity;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.TurnRepository;

@Service
public class ChessService {
	private static final boolean FIRST_TURN = true;

	private final BoardRepository boardRepository;
	private final TurnRepository turnRepository;

	public ChessService(BoardRepository boardRepository, TurnRepository turnRepository) {
		this.boardRepository = boardRepository;
		this.turnRepository = turnRepository;
	}

	public Board move(Position start, Position target) {
		Board board = init();
		Piece startPiece = board.findByPosition(start);
		board.move(start, target);
		boardDao.update(target, startPiece.getName());
		boardDao.update(start, Blank.NAME);
		turnDao.changeTurn(board.isWhiteTurn());
		return board;
	}

	public Board init() {
		List<BoardEntity> pieces = findPieces();
		TurnEntity turn = findTurn();
		if (pieces.isEmpty()) {
			return createBoard(BoardFactory.createBoard());
		}
		pieces.stream()
			.map(boardEntity ->)
		return BoardFactory.createBoard(pieceDtos, turn);
	}

	private List<BoardEntity> findPieces() {
		return boardRepository.findAll();
	}

	private TurnEntity findTurn() {
		return turnRepository.findById(1L)
			.orElseGet(() -> turnRepository.save(TurnEntity.of(1L, FIRST_TURN)));
	}

	private Board createBoard(Board board) {
		List<Piece> pieces = board.findAll();
		for (Piece piece : pieces) {
			String position = piece.getPosition().getString();
			String name = piece.getName();
			boardRepository.save(BoardEntity.of(1L, position, name));
		}
		return board;
	}

	public Board restart() {
		boardRepository.deleteAll();
		turnRepository.deleteAll();
		return init();
	}

	public boolean isNotEnd() {
		Board board = init();
		return board.isLiveBothKing();
	}

	public boolean isWinWhiteTeam() {
		Board board = init();
		return board.isLiveKing(Team.WHITE);
	}

	public Team findWinningTeam() {
		Board board = init();
		return Arrays.stream(Team.values())
			.filter(board::isLiveKing)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("승리팀이 없습니다."));
	}

	public Result status() {
		Board board = init();
		Status status = board.createStatus();
		return status.getResult();
	}
}
