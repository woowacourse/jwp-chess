package wooteco.chess.service;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.exception.InvalidTurnException;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.domain.piece.exception.NotMovableException;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.dto.StatusDto;
import wooteco.chess.dto.TurnDto;
import wooteco.chess.entity.ChessGameEntity;
import wooteco.chess.entity.ChessGameRepository;
import wooteco.chess.service.exception.InvalidGameException;

@Service
public class ChessGameService {
	private final ChessGameRepository chessGameRepository;

	public ChessGameService(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	public ResponseDto games() {
		return new ResponseDto(ResponseDto.SUCCESS, chessGameRepository.findRoomIds());
	}

	public ResponseDto find(String id) {
		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(id)
				.orElseThrow(InvalidGameException::new);
		return new ResponseDto(ResponseDto.SUCCESS, convertToChessGameDto(persistChessGameEntity.toDomain()));
	}

	public ResponseDto move(String id, Position source, Position target) {
		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(id)
				.orElseThrow(InvalidGameException::new);
		ChessGame chessGame = persistChessGameEntity.toDomain();
		try {
			chessGame.move(source, target);
			persistChessGameEntity.update(chessGame);
			ChessGameEntity result = chessGameRepository.save(persistChessGameEntity);

			return new ResponseDto(ResponseDto.SUCCESS, convertToChessGameDto(chessGame));
		} catch (NotMovableException | IllegalArgumentException e) {
			return new ResponseDto(ResponseDto.FAIL, "이동할 수 없는 위치입니다.");
		} catch (InvalidTurnException e) {
			return new ResponseDto(ResponseDto.FAIL, chessGame.turn().getColor() + "의 턴입니다.");
		}
	}

	public ResponseDto create() {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
		ChessGameEntity persistChessGameEntity = chessGameRepository.save(chessGameEntity);
		return new ResponseDto(ResponseDto.SUCCESS, persistChessGameEntity.getId());
	}

	public ResponseDto restart(String id) {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		ChessGameEntity persistChessGameEntity = chessGameRepository.findById(id)
				.orElseThrow(InvalidGameException::new);
		persistChessGameEntity.update(chessGame);
		chessGameRepository.save(persistChessGameEntity);
		return new ResponseDto(ResponseDto.SUCCESS, id);
	}

	public ResponseDto delete(String id) {
		chessGameRepository.deleteById(id);
		return new ResponseDto(ResponseDto.SUCCESS, null);
	}

	private ChessGameDto convertToChessGameDto(ChessGame chessGame) {
		return new ChessGameDto(new BoardDto(chessGame.board()), new TurnDto(chessGame.turn()),
				new StatusDto(chessGame.status().getWhiteScore(), chessGame.status().getBlackScore(),
						chessGame.status().getWinner()), chessGame.isFinished());
	}
}
