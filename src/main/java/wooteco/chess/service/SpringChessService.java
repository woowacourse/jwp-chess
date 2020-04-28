package wooteco.chess.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.entity.ChessGameEntity;
import wooteco.chess.domain.entity.PieceEntity;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.piece.PiecesFactory;
import wooteco.chess.domain.piece.Team;
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

	public void initialize(String gameId) {
		if (chessGameRepository.findByRoomId(gameId).isEmpty()) {
			List<Piece> initialPieces = Board.of(PiecesFactory.createInitial()).getPieces();

			pieceRepository.saveAll(initialPieces.stream()
				.map(piece -> PieceEntity.of(gameId, piece))
				.collect(toList()));

			chessGameRepository.save(new ChessGameEntity(gameId, Team.WHITE.name()));
		}
	}

	public Map<String, String> getBoard(String gameId) {
		Board board = Board.of(pieceRepository.findAllByRoomId(gameId).stream()
			.map(PieceFactory::createBy)
			.collect(toList()));

		return board.getPieces().stream()
			.collect(toMap(piece -> piece.getPosition().getName(), Piece::getSymbol));
	}

	public void move(String gameId, Position from, Position to) {
		Board board = Board.of(pieceRepository.findAllByRoomId(gameId).stream()
			.map(PieceFactory::createBy)
			.collect(toList()));
		ChessGameEntity chessGameEntity = chessGameRepository.findByRoomId(gameId)
			.orElseThrow(() -> new IllegalArgumentException("게임이 존재하지 않습니다. game id = " + gameId));

		board.verifyMove(from, to, Team.valueOf(chessGameEntity.getTurn()));

		PieceEntity source = pieceRepository.findByGameIdAndPosition(gameId, from.getName())
			.orElseThrow(() -> new IllegalArgumentException("기물이 존재하지 않습니다. position : " + from.getName()));
		PieceEntity target = pieceRepository.findByGameIdAndPosition(gameId, to.getName())
			.orElseThrow(() -> new IllegalArgumentException("기물이 존재하지 않습니다. position : " + to.getName()));

		target.updateSymbolToSource(source);
		source.updateSymbolToEmpty();
		chessGameEntity.updateTurnToNext();

		pieceRepository.save(target);
		pieceRepository.save(source);
		chessGameRepository.save(chessGameEntity);
	}
}
