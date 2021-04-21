package chess.view;

import chess.dto.GameInfoDto;
import chess.dto.RoomDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelView {
    private ModelView() {
    }

    public static Map<String, Object> idResponse(String id) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", id);
        return model;
    }

    public static Map<String, Object> gameResponse(GameInfoDto gameInfoDto, String id) {
        Map<String, Object> model = new HashMap<>();
        model.put("squares", gameInfoDto.squares());
        model.put("turn", gameInfoDto.turn());
        model.put("scores", gameInfoDto.scores());
        model.put("roomId", id);
        return model;
    }

    public static Map<String, Object> roomResponse(List<RoomDto> room) {
        Map<String, Object> model = new HashMap<>();
        if (!room.isEmpty()) {
            model.put("room", room);
        }
        return model;
    }
}
