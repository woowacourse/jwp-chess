package wooteco.chess.service;

import static wooteco.chess.domain.board.Board.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.Score;
import wooteco.chess.domain.board.ChessGame;
import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.command.MoveCommand;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.team.Team;
import wooteco.chess.dao.PieceDao;
import wooteco.chess.dao.TurnDao;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.PieceDto;
import wooteco.chess.dto.TurnDto;
import wooteco.chess.util.ScoreConverter;
import wooteco.chess.util.UnicodeConverter;

public class ChessService {
	private static final String BLANK = "";
	private PieceDao pieceDao = PieceDao.getInstance();
	private TurnDao turnDao = TurnDao.getInstance();

	public BoardDto createPieces() throws SQLException {
		ChessGame chessGame = new ChessGame();
		addTurn(chessGame);
		return addBoard(chessGame);
	}

	public BoardDto move(String command) throws SQLException {
		List<Piece> pieces = pieceDao.findAll();
		TurnDto turnDto = turnDao.find();

		ChessGame chessGame = new ChessGame(pieces, turnDto.getTurn());
		MoveCommand moveCommand = new MoveCommand(command);
		chessGame.move(moveCommand);

		String originalPosition = moveCommand.getSourcePosition().toString();
		String newPosition = moveCommand.getTargetPosition().toString();
		updateTurn(chessGame);
		return updateBoard(chessGame, originalPosition, newPosition);
	}

	public BoardDto load() throws SQLException {
		List<Piece> pieces = pieceDao.findAll();
		TurnDto turnDto = turnDao.find();
		ChessGame chessGame = new ChessGame(pieces, turnDto.getTurn());

		return createBoardDto(chessGame);
	}

	private void addTurn(ChessGame chessGame) throws SQLException {
		Team turn = chessGame.getTurn();
		TurnDto turnDto = new TurnDto(turn.getName());
		turnDao.add(turnDto);
	}

	private void updateTurn(ChessGame chessGame) throws SQLException {
		Team turn = chessGame.getTurn();
		TurnDto turnDto = new TurnDto(turn.getName());
		turnDao.update(turnDto);
	}

	private BoardDto addBoard(ChessGame chessGame) throws SQLException {
		List<Piece> pieces = chessGame.getPieces();
		for (Piece piece : pieces) {
			addPieces(piece);
		}
		return createBoardDto(chessGame);
	}

	private void addPieces(Piece piece) throws SQLException {
		Team team = piece.getTeam();
		String position = piece.getPosition().toString();
		PieceDto pieceDto = new PieceDto(piece.showSymbol(), team.getName(), position);
		pieceDao.add(pieceDto);
	}

	private BoardDto updateBoard(ChessGame chessGame, String originalPosition, String newPosition) throws
		SQLException {
		pieceDao.update(originalPosition, newPosition);
		return createBoardDto(chessGame);
	}

	private BoardDto createBoardDto(ChessGame chessGame) {
		List<String> symbols = convertToUnicode(chessGame.getReverse());
		Map<Team, Double> score = Score.calculateScore(chessGame.getPieces(), Team.values());
		return new BoardDto(symbols, ScoreConverter.convert(score));
	}

	private List<String> convertToUnicode(List<Rank> board) {
		List<String> pieces = new ArrayList<>();
		for (Rank rank : board) {
			convertRank(pieces, rank);
		}
		return pieces;
	}

	private void convertRank(List<String> pieces, Rank rank) {
		for (int i = MIN_COLUMN_COUNT; i <= MAX_COLUMN_COUNT; i++) {
			final int columnNumber = i;
			String pieceSymbol = rank.getPieces().stream()
				.filter(p -> p.equalsColumn(columnNumber))
				.map(Piece::showSymbol)
				.findFirst()
				.orElse(BLANK);
			pieces.add(UnicodeConverter.convert(pieceSymbol));
		}
	}
}