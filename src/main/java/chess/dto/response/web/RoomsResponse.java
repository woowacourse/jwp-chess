package chess.dto.response.web;

import chess.domain.entity.Game;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RoomsResponse {

    private final List<RoomResponse> values = new ArrayList<>();

    public RoomsResponse(List<Game> games) {
        for (Game game : games) {
            RoomResponse roomResponse = new RoomResponse(
                    game.getGameId(),
                    game.getRoomName(),
                    game.getCreatedAt()
            );
            values.add(roomResponse);
        }
    }

    @AllArgsConstructor
    @Getter
    private static class RoomResponse {
        private final String gameId;
        private final String roomName;
        private final LocalDateTime createdAt;
    }
}
