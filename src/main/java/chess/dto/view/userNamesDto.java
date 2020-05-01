package chess.dto.view;

import chess.model.domain.piece.Team;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class userNamesDto {

    private String whiteName;
    private String blackName;

    protected userNamesDto() {
    }

    public Map<Team, String> findUserNames() {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, updateName(blackName, "BLACK"));
        userNames.put(Team.WHITE, updateName(whiteName, "WHITE"));
        return Collections.unmodifiableMap(userNames);
    }

    public String updateName(String name, String defaultName) {
        if (Objects.isNull(name) || name.isEmpty()) {
            System.out.println("Default");
            return defaultName;
        }
        return name;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }
}