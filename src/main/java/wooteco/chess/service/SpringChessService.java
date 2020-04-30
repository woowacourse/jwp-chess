package wooteco.chess.service;

import static java.util.stream.Collectors.*;

import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.entity.ChessGame;
import wooteco.chess.domain.entity.PieceEntity;
import wooteco.chess.domain.piece.PiecesFactory;
import wooteco.chess.domain.piece.Turn;
import wooteco.chess.domain.position.Position;
import wooteco.chess.repository.ChessGameRepository;
import wooteco.chess.repository.PieceRepository;

@Service
public class SpringChessService {
	private ChessGameRepository chessGameRepository;
	private PieceRepository pieceRepository;

	public SpringChessService(ChessGameRepository chessGameRepository, PieceRepository pieceRepository) {
		this.chessGameRepository = chessGameRepository;
		this.pieceRepository = pieceRepository;
	}

	public void initialize(String roomId) {
		if (chessGameRepository.findByRoomId(roomId).isEmpty()) {
			Set<PieceEntity> pieceEntities = PiecesFactory.createInitial().stream()
				.map(PieceEntity::of)
				.collect(toSet());
			chessGameRepository.save(new ChessGame(roomId, Turn.WHITE, pieceEntities));
		}
	}

	public Set<PieceEntity> getBoard(String roomId) {
		return chessGameRepository.findByRoomId(roomId)
			.stream()
			.map(ChessGame::getPieces)
			.findFirst()
			.orElseThrow();
	}

	public void move(String gameId, Position from, Position to) {
		ChessGame chessGame = chessGameRepository.findByRoomId(gameId)
			.orElseThrow(() -> new IllegalArgumentException("게임이 존재하지 않습니다. game id = " + gameId));
		Board board = Board.of(chessGame.getPieces());

		PieceEntity source = pieceRepository.findByGameIdAndPosition(chessGame.getId(), from.getName())
			.orElseThrow(() -> new IllegalArgumentException("기물이 존재하지 않습니다. position : " + from.getName()));
		PieceEntity target = pieceRepository.findByGameIdAndPosition(chessGame.getId(), to.getName())
			.orElseThrow(() -> new IllegalArgumentException("기물이 존재하지 않습니다. position : " + to.getName()));

		board.move(source, target, chessGame);

		pieceRepository.save(target);
		pieceRepository.save(source);
		chessGameRepository.save(chessGame);
	}
}
