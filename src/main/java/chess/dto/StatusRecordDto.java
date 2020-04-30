package chess.dto;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class StatusRecordDto {
	@Id
	private int id;
	private final String record;
	private final Date gameDate;
	private final String roomName;

	public StatusRecordDto(final String record, final Date gameDate, final String roomName) {
		this.record = record;
		this.gameDate = gameDate;
		this.roomName = roomName;
	}

	public int getId() {
		return id;
	}

	public String getRecord() {
		return record;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public String getRoomName() {
		return roomName;
	}

	@Override
	public String toString() {
		return "StatusRecordDto{" +
				"id=" + id +
				", record='" + record + '\'' +
				", gameDate=" + gameDate +
				", roomName='" + roomName + '\'' +
				'}';
	}
}
