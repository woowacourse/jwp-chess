package chess.view;

import chess.domain.dto.GameInfoDto;
import chess.domain.dto.HistoryDto;

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
        model.put("gameId", id);
        return model;
    }

    public static Map<String, Object> historyResponse(List<HistoryDto> history) {
        Map<String, Object> model = new HashMap<>();
        if (!history.isEmpty()) {
            model.put("history", history);
        }
        return model;
    }
}
