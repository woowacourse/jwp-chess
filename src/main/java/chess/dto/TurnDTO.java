package chess.dto;

import static chess.util.NullValidator.*;

import chess.domain.board.ChessBoard;
import chess.domain.piece.PieceColor;

public class TurnDTO {
	private String currentTeam;

	private TurnDTO(String currentTeam) {
		validateNull(currentTeam);
		this.currentTeam = currentTeam;
	}

	public static TurnDTO from(ChessBoard chessBoard) {
		validateNull(chessBoard);

		String currentTeam = chessBoard.getTeam().getName();

		return from(currentTeam);
	}

	public static TurnDTO from(String currentTeam) {
		validateNull(currentTeam);

		return new TurnDTO(currentTeam);
	}

	public String getCurrentTeam() {
		return this.currentTeam;
	}

	public PieceColor createTeam() {
		return PieceColor.of(this.currentTeam);
	}
}