package chess.webdao;

public class ChessGameInfo {
    private String current_turn_team;
    private boolean is_playing;

    public ChessGameInfo(String current_turn_team, boolean is_playing) {
        this.current_turn_team = current_turn_team;
        this.is_playing = is_playing;
    }

    public String getCurrentTurnTeam() {
        return current_turn_team;
    }

    public boolean getIsPlaying() {
        return is_playing;
    }
}
