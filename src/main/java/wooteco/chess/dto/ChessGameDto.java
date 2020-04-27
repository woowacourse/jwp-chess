package wooteco.chess.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;
import wooteco.chess.domain.Side;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Row;

public class ChessGameDto {
	public static final String BLANK = "blank";

	private String roomName;
	private Map<String, String> board;
	private String turn;
	private String finishFlag;
	private double whiteScore;
	private double blackScore;

	public ChessGameDto(String roomName, Map<String, String> board, String turn, String finishFlag, double whiteScore,
			double blackScore) {
		this.roomName = roomName;
		this.board = board;
		this.turn = turn;
		this.finishFlag = finishFlag;
		this.whiteScore = whiteScore;
		this.blackScore = blackScore;
	}

	public static ChessGameDto of(String roomName, ChessGame chessGame) {
		Map<String, String> board = new HashMap<>();
		makeBlankBoard(board, chessGame);
		deployPiece(board, chessGame);
		String turn = chessGame.getTurn().name();
		String finishFlag = FinishFlag.of(chessGame.isEnd()).getSymbol();
		double whiteScore = chessGame.status(Side.WHITE);
		double blackScore = chessGame.status(Side.BLACK);
		return new ChessGameDto(roomName, board, turn, finishFlag, whiteScore, blackScore);
	}

	private static void makeBlankBoard(Map<String, String> board, ChessGame chessGame) {
		for (Row row : Row.values()) {
			for (Column col : Column.values()) {
				board.put(col.getSymbol() + row.getSymbol(), BLANK);
			}
		}
	}

	private static void deployPiece(Map<String, String> board, ChessGame chessGame) {
		List<Piece> pieces = chessGame.getBoard().getPieces();
		for (Piece piece : pieces) {
			board.put(piece.getPosition().toString(), makeName(piece));
		}
	}

	private static String makeName(Piece piece) {
		String name = piece.getName() + "_" + piece.getSide();
		return name.toLowerCase();
	}

	public String getRoomName() {
		return roomName;
	}

	public Map<String, String> getBoard() {
		return board;
	}

	public String getTurn() {
		return turn;
	}

	public String getFinishFlag() {
		return finishFlag;
	}

	public double getWhiteScore() {
		return whiteScore;
	}

	public double getBlackScore() {
		return blackScore;
	}
}
