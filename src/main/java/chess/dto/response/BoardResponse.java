package chess.dto.response;

import java.util.Map;

public class BoardResponse {

    private Map<String, String> values;

    private BoardResponse(Map<String, String> values) {
        this.values = values;
    }

    public static BoardResponse from(final Map<String, String> squares) {
        return new BoardResponse(squares);
    }

    public Map<String, String> getValues() {
        return values;
    }
}
