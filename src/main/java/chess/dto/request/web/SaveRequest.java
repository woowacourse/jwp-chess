package chess.dto.request.web;

import java.util.Map;

public class SaveRequest {

    private String currentTeam;
    private Map<String, String> pieces;

    public String getCurrentTeam() {
        return currentTeam;
    }

    public Map<String, String> getPieces() {
        return pieces;
    }

    public void setCurrentTeam(String currentTeam) {
        this.currentTeam = currentTeam;
    }

    public void setPieces(Map<String, String> pieces) {
        this.pieces = pieces;
    }
}
