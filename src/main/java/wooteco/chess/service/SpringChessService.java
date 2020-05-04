package wooteco.chess.service;

import static java.util.stream.Collectors.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.Status;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.entity.ChessGame;
import wooteco.chess.domain.entity.PieceEntity;
import wooteco.chess.domain.piece.PiecesFactory;
import wooteco.chess.domain.piece.Turn;
import wooteco.chess.domain.position.Position;
import wooteco.chess.repository.ChessGameRepository;

@Service
public class SpringChessService {
	private ChessGameRepository chessGameRepository;

	public SpringChessService(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	public List<ChessGame> getGames() {
		return chessGameRepository.findAll();
	}

	public void initialize(String roomId) {
		if (chessGameRepository.findByRoomId(roomId).isEmpty()) {
			Set<PieceEntity> pieceEntities = PiecesFactory.createInitial().stream()
				.map(PieceEntity::of)
				.collect(toSet());
			chessGameRepository.save(new ChessGame(roomId, Turn.WHITE, pieceEntities));
		}
	}

	public Map<String, String> getBoard(String roomId) {
		Set<PieceEntity> pieceEntities = chessGameRepository.findByRoomId(roomId)
			.stream()
			.map(ChessGame::getPieces)
			.findFirst()
			.orElseThrow();

		return pieceEntities.stream()
			.collect(toMap(pieceEntity -> pieceEntity.getPosition().getName(), PieceEntity::getSymbol));
	}

	public void move(String roomId, Position from, Position to) {
		ChessGame chessGame = chessGameRepository.findByRoomId(roomId)
			.orElseThrow(() -> new IllegalArgumentException("게임이 존재하지 않습니다. game id = " + roomId));
		Board board = Board.of(chessGame.getPieces());

		PieceEntity source = chessGame.findPieceByPosition(from);
		PieceEntity target = chessGame.findPieceByPosition(to);

		board.move(source, target, chessGame);

		chessGameRepository.save(chessGame);
	}

	public Map<String, String> getResult(String roomId) {
		Map<String, String> result = new HashMap<>();
		Set<PieceEntity> pieceEntities = chessGameRepository.findByRoomId(roomId)
			.stream()
			.map(ChessGame::getPieces)
			.findFirst()
			.orElseThrow();

		Status status = Status.of(pieceEntities);

		String whiteScore = String.valueOf(status.toMap().get(Turn.WHITE));
		String blackScore = String.valueOf(status.toMap().get(Turn.BLACK));
		String winner = status.getWinner().name();

		result.put("whiteScore", whiteScore);
		result.put("blackScore", blackScore);
		result.put("result", winner);

		return result;
	}
}
