package chess.domain.chessgame;

import chess.domain.board.Board;

public class ChessGame {

	private final Board board;
	private final ChessGameInfo chessGameInfo;

	private ChessGame(String name, String password, Board board) {
		this.board = board;
		this.chessGameInfo = new ChessGameInfo(name, password);
	}

	private ChessGame(int id, ChessGame chessGame) {
		this.board = chessGame.board;
		this.chessGameInfo = new ChessGameInfo(id, chessGame.getName(), chessGame.getPassword());
	}

	public static ChessGame create(String name, String password, Board board) {
		return new ChessGame(name, password, board);
	}

	public static ChessGame createWithId(int id, ChessGame chessGame) {
		return new ChessGame(id, chessGame);
	}

	public boolean isRightPassword(String password) {
		return this.chessGameInfo.isSamePassword(password);
	}

	public boolean isEnd() {
		return board.isEnd();
	}

	public int getId() {
		return chessGameInfo.getId();
	}

	public String getName() {
		return chessGameInfo.getName();
	}

	public String getPassword() {
		return chessGameInfo.getPassword();
	}

	public Board getBoard() {
		return board;
	}
}
