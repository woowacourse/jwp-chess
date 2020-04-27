package wooteco.chess.entity;

import org.springframework.data.annotation.Id;

public class ChessGame {
	@Id
	private Long id;
	private String board;
	private String turn;
	private Integer roomNo;

	public ChessGame(String board, String turn, Integer roomNo) {
		this.board = board;
		this.turn = turn;
		this.roomNo = roomNo;
	}

	public Long getId() {
		return id;
	}

	public String getBoard() {
		return board;
	}

	public String getTurn() {
		return turn;
	}

	public Integer getRoomNo() {
		return roomNo;
	}
}
