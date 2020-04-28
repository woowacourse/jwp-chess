package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("piece")
public class PieceDto {
	@Id
	int id;
	@Column("piece_type")
	final String pieceType;
	@Column("team")
	final String team;
	@Column("coordinate")
	final String coordinate;
	@Column("room_id")
	final int roomId;

	public PieceDto(final int id, final String pieceType, final String team,
					final String coordinate, final int roomId) {
		this.id = id;
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
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final PieceDto pieceDto = (PieceDto) o;
		return id == pieceDto.id &&
				roomId == pieceDto.roomId &&
				Objects.equals(pieceType, pieceDto.pieceType) &&
				Objects.equals(team, pieceDto.team) &&
				Objects.equals(coordinate, pieceDto.coordinate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pieceType, team, coordinate, roomId);
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
