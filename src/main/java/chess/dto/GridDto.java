package chess.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class GridDto {
    private Long gridId;
    private boolean isBlackTurn;
    private boolean isFinished;
    private Long roomId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    private boolean isStarted;

    public GridDto() {
        super();
    }

    public GridDto(Long gridId, boolean isBlackTurn, boolean isFinished, Long roomId, LocalDateTime createdAt, boolean isStarted) {
        this.gridId = gridId;
        this.isBlackTurn = isBlackTurn;
        this.isFinished = isFinished;
        this.roomId = roomId;
        this.createdAt = createdAt;
        this.isStarted = isStarted;
    }

    public Long getGridId() {
        return gridId;
    }

    public boolean getIsBlackTurn() {
        return isBlackTurn;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean getIsStarted() {
        return isStarted;
    }

    @Override
    public String toString() {
        return "GridDto{" +
                "gridId=" + gridId +
                ", isBlackTurn=" + isBlackTurn +
                ", isFinished=" + isFinished +
                ", roomId=" + roomId +
                ", createdAt=" + createdAt +
                ", isStarted=" + isStarted +
                '}';
    }
}
