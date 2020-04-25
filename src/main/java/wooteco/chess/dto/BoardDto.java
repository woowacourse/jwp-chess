package wooteco.chess.dto;

public class BoardDto {
	private final String pieceName;
	private final String pieceTeam;
	private final String piecePosition;

	public BoardDto(String pieceName, String pieceTeam, String piecePosition) {
		this.pieceName = pieceName;
		this.pieceTeam = pieceTeam;
		this.piecePosition = piecePosition;
	}

	public String pieceName() {
		return pieceName;
	}

	public String pieceTeam() {
		return pieceTeam;
	}

	public String piecePosition() {
		return piecePosition;
	}
}
