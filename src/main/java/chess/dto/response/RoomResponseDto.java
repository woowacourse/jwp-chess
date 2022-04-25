package chess.dto.response;

import chess.domain.GameStatus;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomResponseDto {

    private final int roomId;
    private final String roomName;
    private final GameStatus gameStatus;

    private RoomResponseDto(final int roomId, final String roomName, final GameStatus gameStatus) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.gameStatus = gameStatus;
    }

    public static RoomResponseDto from(final ResultSet resultSet) throws SQLException {
        return new RoomResponseDto(
                Integer.parseInt(resultSet.getString("room_id")),
                resultSet.getString("name"),
                GameStatus.from(resultSet.getString("game_status"))
        );
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
