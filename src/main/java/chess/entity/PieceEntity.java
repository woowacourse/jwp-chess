package chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("piece")
public class PieceEntity {
	@Id
	private int id;
	private final String pieceType;
	private final String team;
	private final String coordinate;
	private final int roomId;

	public PieceEntity(final String pieceType, final String team,
					   final String coordinate, final int roomId) {
		this.pieceType = pieceType;
		this.team = team;
		this.coordinate = coordinate;
		this.roomId = roomId;
	}

	public int getId() {
		return id;
	}

	public String getPieceType() {
		return pieceType;
	}

	public String getTeam() {
		return team;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public int getRoomId() {
		return roomId;
	}

	@Override
	public String toString() {
		return "Piece{" +
				"id=" + id +
				", pieceType='" + pieceType + '\'' +
				", team='" + team + '\'' +
				", coordinate='" + coordinate + '\'' +
				", roomId=" + roomId +
				'}';
	}
}
