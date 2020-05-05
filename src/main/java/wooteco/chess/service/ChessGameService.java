package wooteco.chess.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.entity.ChessGameEntity;
import wooteco.chess.entity.ChessGameRepository;
import wooteco.chess.service.exception.InvalidGameException;

@Service
public class ChessGameService {
	private final ChessGameRepository chessGameRepository;

	public ChessGameService(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	public List<Long> games() {
		return chessGameRepository.findRoomIds();
	}

	public ChessGameDto find(Long id) {
		return ChessGameDto.from(chessGameRepository.findById(id)
				.orElseThrow(InvalidGameException::new));
	}

	public ChessGameDto move(Long id, Position source, Position target) {
		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(id)
				.orElseThrow(InvalidGameException::new);
		ChessGame chessGame = persistChessGameEntity.toDomain();
		chessGame.move(source, target);
		persistChessGameEntity.update(chessGame);
		return ChessGameDto.from(chessGameRepository.save(persistChessGameEntity));
	}

	public Long create() {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		return chessGameRepository.save(new ChessGameEntity(chessGame))
				.getId();
	}

	public void restart(Long id) {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(id)
				.orElseThrow(InvalidGameException::new);
		persistChessGameEntity.update(chessGame);
		chessGameRepository.save(persistChessGameEntity);
	}

	public void delete(Long id) {
		chessGameRepository.deleteById(id);
	}
}
