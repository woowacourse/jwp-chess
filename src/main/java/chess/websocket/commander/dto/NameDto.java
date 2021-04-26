package chess.websocket.commander.dto;

public class NameDto {

    private String whiteName;
    private String blackName;

    public NameDto(String whiteName, String blackName) {
        this.whiteName = whiteName;
        this.blackName = blackName;
    }

    public NameDto() { }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }
}

