package wooteco.chess.service;

import java.util.Arrays;

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
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.PieceRepository;
import wooteco.chess.repository.TurnRepository;

@Service
public class ChessService {
	private final BoardRepository boardRepository;
	private final TurnRepository turnRepository;
	private final PieceRepository pieceRepository;

	public ChessService(BoardRepository boardRepository, TurnRepository turnRepository,
		PieceRepository pieceRepository) {
		this.boardRepository = boardRepository;
		this.turnRepository = turnRepository;
		this.pieceRepository = pieceRepository;
	}

	public void move(Position start, Position target, Long boardId) {
		Board board = find();
		Piece startPiece = board.findByPosition(start);

		board.move(start, target);
		pieceRepository.update(startPiece.getName(), target.getString(), boardId);
		pieceRepository.update(Blank.NAME, start.getString(), boardId);
		turnRepository.update(board.isWhiteTurn(), boardId);
	}

	public Board find() {
		BoardEntity boardEntity = boardRepository.findById(1L)
			.orElseGet(this::init);
		return boardEntity.createBoard();

	}

	private BoardEntity init() {
		return boardRepository.save(BoardEntity.from(BoardFactory.create()));
	}

	public Board restart() {
		boardRepository.deleteAll();
		turnRepository.deleteAll();
		return find();
	}

	public boolean isEnd() {
		Board board = find();
		return !board.isLiveBothKing();
	}

	public Team findWinningTeam() {
		Board board = find();
		return Arrays.stream(Team.values())
			.filter(board::isLiveKing)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("승리팀이 없습니다."));
	}

	public Result status() {
		Board board = find();
		Status status = board.createStatus();
		return status.getResult();
	}
}