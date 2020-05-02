package wooteco.chess.dto;

import static wooteco.chess.util.NullValidator.*;

import wooteco.chess.domain.board.ChessBoard;
import wooteco.chess.domain.piece.PieceColor;

public class TurnDto {
	private String currentTeam;

	private TurnDto(String currentTeam) {
		validateNull(currentTeam);
		this.currentTeam = currentTeam;
	}

	public static TurnDto from(ChessBoard chessBoard) {
		validateNull(chessBoard);

		String currentTeam = chessBoard.getTeam().getName();

		return from(currentTeam);
	}

	public static TurnDto from(String currentTeam) {
		validateNull(currentTeam);

		return new TurnDto(currentTeam);
	}

	public PieceColor createTeam() {
		return PieceColor.of(this.currentTeam);
	}

	public String getCurrentTeam() {
		return this.currentTeam;
	}
}