package chess.dto;

import java.util.Map;

public class StatusDto {

    private Map<String, Double> status;

    public StatusDto(Map<String, Double> status) {
        this.status = status;
    }

    public Map<String, Double> getStatus() {
        return status;
    }
}
