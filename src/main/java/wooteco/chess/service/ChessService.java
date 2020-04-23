package wooteco.chess.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import wooteco.chess.dao.GameDao;
import wooteco.chess.dao.PieceDao;
import wooteco.chess.domain.Score;
import wooteco.chess.domain.board.ChessGame;
import wooteco.chess.domain.command.MoveCommand;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.dto.PieceDto;
import wooteco.chess.util.ScoreConverter;
import wooteco.chess.util.UnicodeConverter;

public class ChessService {
	private PieceDao pieceDao = PieceDao.getInstance();
	private GameDao gameDao = GameDao.getInstance();

	public BoardDto createGame() throws SQLException {
		ChessGame chessGame = new ChessGame();
		Long gameId = saveGame(chessGame)
			.getId();
		return savePieces(gameId, chessGame);
	}

	public MoveResponseDto move(MoveRequestDto moveRequestDto) throws SQLException {
		Long gameId = moveRequestDto.getGameId();
		String command = moveRequestDto.getCommand();

		List<Piece> pieces = pieceDao.findAllByGameId(gameId);
		GameDto gameDto = gameDao.findById(gameId)
			.orElseThrow(IllegalArgumentException::new);

		ChessGame chessGame = new ChessGame(pieces, gameDto.getTurn());
		MoveCommand moveCommand = new MoveCommand(command);
		chessGame.move(moveCommand);

		String originalPosition = moveCommand.getSourcePosition().getName();
		String newPosition = moveCommand.getTargetPosition().getName();
		updateGame(gameId, chessGame);
		return updateBoard(chessGame, gameId, originalPosition, newPosition);
	}

	public BoardDto load(Long gameId) throws SQLException {
		GameDto gameDto = gameDao.findById(gameId)
			.orElseThrow(() -> new IllegalArgumentException(String.format("%d : 존재하지 않는 게임입니다.", gameId)));
		List<Piece> pieces = pieceDao.findAllByGameId(gameId);
		ChessGame chessGame = new ChessGame(pieces, gameDto.getTurn());

		return createBoardDto(gameId, chessGame);
	}

	private GameDto saveGame(ChessGame chessGame) throws SQLException {
		Team turn = chessGame.getTurn();
		GameDto gameDto = new GameDto(turn.getName());
		return gameDao.save(gameDto);
	}

	private BoardDto savePieces(Long gameId, ChessGame chessGame) throws SQLException {
		List<Piece> pieces = chessGame.getPieces();
		for (Piece piece : pieces) {
			savePiece(gameId, piece);
		}
		return createBoardDto(gameId, chessGame);
	}

	private void updateGame(Long id, ChessGame chessGame) throws SQLException {
		Team turn = chessGame.getTurn();
		GameDto gameDto = new GameDto(id, turn.getName());
		gameDao.update(gameDto);
	}

	private void savePiece(Long gameId, Piece piece) throws SQLException {
		Team team = piece.getTeam();
		String position = piece.getPosition().getName();
		String symbol = piece.getSymbol();
		PieceDto pieceDto = new PieceDto(gameId, symbol, team.getName(), position);
		pieceDao.save(pieceDto);
	}

	private MoveResponseDto updateBoard(ChessGame chessGame, Long gameId, String originalPosition,
		String targetPosition) throws
		SQLException {
		PieceDto originalPiece = pieceDao.findByGameIdAndPosition(gameId, originalPosition)
			.orElseThrow(IllegalArgumentException::new);
		pieceDao.deleteByGameIdAndPosition(gameId, targetPosition);
		pieceDao.update(originalPiece.getId(), targetPosition);
		return createMoveResponseDto(gameId, chessGame, Position.of(targetPosition));
	}

	private MoveResponseDto createMoveResponseDto(Long gameId, ChessGame chessGame, Position position) {
		Piece piece = chessGame.findPieceByPosition(position);

		return new MoveResponseDto(gameId, chessGame.isKingAlive(), UnicodeConverter.convert(piece.getSymbol()),
			chessGame.getTurn().getName());
	}

	private BoardDto createBoardDto(Long gameId, ChessGame chessGame) {
		List<String> symbols = UnicodeConverter.convert(chessGame.getReverse());
		Map<Team, Double> score = Score.calculateScore(chessGame.getPieces(), Team.values());
		return new BoardDto(gameId, symbols, ScoreConverter.convert(score), chessGame.getTurn().getName());
	}
}