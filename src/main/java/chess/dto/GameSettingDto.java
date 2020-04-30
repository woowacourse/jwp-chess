package chess.dto;

import chess.model.domain.piece.Team;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameSettingDto {

    private String whiteName;
    private String blackName;
    private String way;

    protected GameSettingDto() {
    }

    public Map<Team, String> getUserNames() {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, blackName);
        userNames.put(Team.WHITE, whiteName);
        return Collections.unmodifiableMap(userNames);
    }

    public String getWay() {
        return way;
    }
}