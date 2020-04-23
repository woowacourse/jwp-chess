package wooteco.chess.service;

import wooteco.chess.dao.ChessGameDao;
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
import wooteco.chess.service.exception.InvalidGameException;

public class ChessGameService {
	private final ChessGameDao chessGameDao;

	public ChessGameService(ChessGameDao chessGameDao) {
		this.chessGameDao = chessGameDao;
	}

	public ResponseDto games() throws Exception {
		return new ResponseDto(ResponseDto.SUCCESS, chessGameDao.findAll());
	}

	public ResponseDto find(int id) throws Exception {
		ChessGame chessGame = chessGameDao.findById(id).orElseThrow(InvalidGameException::new);
		return new ResponseDto(ResponseDto.SUCCESS, convertToChessGameDto(chessGame));
	}

	public ResponseDto move(int id, Position source, Position target) throws Exception {
		ChessGame chessGame = chessGameDao.findById(id)
				.orElseThrow(InvalidGameException::new);
		try {
			chessGame.move(source, target);
			chessGameDao.updateById(id, chessGame);
			return new ResponseDto(ResponseDto.SUCCESS, convertToChessGameDto(chessGame));
		} catch (NotMovableException | IllegalArgumentException e) {
			return new ResponseDto(ResponseDto.FAIL, "이동할 수 없는 위치입니다.");
		} catch (InvalidTurnException e) {
			return new ResponseDto(ResponseDto.FAIL, chessGame.turn().getColor() + "의 턴입니다.");
		}
	}

	public ResponseDto create() throws Exception {
		int chessGameId = chessGameDao.create();
		return restart(chessGameId);
	}

	public ResponseDto restart(int id) throws Exception {
		ChessGame chessGame = new ChessGame(new Ready());
		chessGame.start();
		chessGameDao.updateById(id, chessGame);
		return new ResponseDto(ResponseDto.SUCCESS, id);
	}

	public ResponseDto delete(int id) throws Exception {
		chessGameDao.deleteById(id);
		return new ResponseDto(ResponseDto.SUCCESS, null);
	}

	private ChessGameDto convertToChessGameDto(ChessGame chessGame) {
		return new ChessGameDto(new BoardDto(chessGame.board()), new TurnDto(chessGame.turn()),
				new StatusDto(chessGame.status().getWhiteScore(), chessGame.status().getBlackScore(),
						chessGame.status().getWinner()), chessGame.isFinished());
	}
}
