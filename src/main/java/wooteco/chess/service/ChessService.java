package wooteco.chess.service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.TurnDao;
import wooteco.chess.domain.Result;
import wooteco.chess.domain.Status;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.factory.BoardFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDto;

@Service
public class ChessService {
	private static final boolean FIRST_TURN = true;

	private final BoardDao boardDao;
	private final TurnDao turnDao;

	public ChessService(final BoardDao boardDao, final TurnDao turnDao) {
		this.boardDao = boardDao;
		this.turnDao = turnDao;
	}

	public Board move(Position startPosition, Position targetPosition) {
		Board board = init();
		Piece startPiece = board.findByPosition(startPosition);
		board.move(startPosition, targetPosition);
		boardDao.update(targetPosition, startPiece.getName());
		boardDao.update(startPosition, Blank.NAME);
		turnDao.changeTurn(board.isWhiteTurn());
		return board;
	}

	public Board init() {
		List<PieceDto> pieceDtos = findPieces();
		Turn turn = findTurn();
		if (pieceDtos.isEmpty()) {
			return createBoard(BoardFactory.createBoard());
		}
		return BoardFactory.createBoard(pieceDtos, turn);
	}

	private List<PieceDto> findPieces() {
		return boardDao.findAll();
	}

	private Turn findTurn() {
		try {
			return turnDao.find();
		} catch (NoSuchElementException e) {
			Turn turn = new Turn(FIRST_TURN);
			turnDao.addTurn(FIRST_TURN);
			return turn;
		}
	}

	private Board createBoard(Board board) {
		List<Piece> pieces = board.findAll();
		for (Piece piece : pieces) {
			boardDao.addPiece(PieceDto.from(piece));
		}
		return board;
	}

	public Board restart() {
		boardDao.removeAll();
		turnDao.removeAll();
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
