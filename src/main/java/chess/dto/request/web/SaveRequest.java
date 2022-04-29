package chess.dto.request.web;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;

@Getter
public class SaveRequest {

    private String currentTeam;

    private Map<String, String> pieces;

    private LocalDateTime createdAt;

    public SaveRequest() {
    }

    public SaveRequest(String currentTeam, Map<String, String> pieces, LocalDateTime createdAt) {
        this.currentTeam = currentTeam;
        this.pieces = pieces;
        this.createdAt = createdAt;
    }
}
