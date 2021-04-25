package chess.webdto;

public class TurnDto {
    private String turn;
    private boolean isPlaying;

    public TurnDto(){
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public void setIsPlaying (boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getTurn() {
        return turn;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }
}
