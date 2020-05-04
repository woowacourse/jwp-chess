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
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.repository.PieceRepository;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.TurnRepository;

@Service
public class ChessService {
	private final RoomRepository roomRepository;
	private final TurnRepository turnRepository;
	private final PieceRepository pieceRepository;

	public ChessService(RoomRepository roomRepository, TurnRepository turnRepository,
		PieceRepository pieceRepository) {
		this.roomRepository = roomRepository;
		this.turnRepository = turnRepository;
		this.pieceRepository = pieceRepository;
	}

	public void move(Long roomId, Position start, Position target) {
		Board board = find(roomId);
		Piece startPiece = board.findByPosition(start);

		board.move(start, target);
		pieceRepository.update(startPiece.getName(), target.getString(), roomId);
		pieceRepository.update(Blank.NAME, start.getString(), roomId);
		turnRepository.update(board.isWhiteTurn(), roomId);
	}

	public Board find(Long roomId) {
		RoomEntity roomEntity = roomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
		return roomEntity.createBoard();

	}

	public void init(String title) {
		roomRepository.save(RoomEntity.from(title, BoardFactory.create()));
	}

	public Board restart(Long roomId) {
		roomRepository.deleteAll();
		turnRepository.deleteAll();
		return find(roomId);
	}

	public boolean isEnd(Long roomId) {
		Board board = find(roomId);
		return !board.isLiveBothKing();
	}

	public Team findWinningTeam(Long roomId) {
		Board board = find(roomId);
		return Arrays.stream(Team.values())
			.filter(board::isLiveKing)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("승리팀이 없습니다."));
	}

	public Result status(Long roomId) {
		Board board = find(roomId);
		Status status = board.createStatus();
		return status.getResult();
	}
}