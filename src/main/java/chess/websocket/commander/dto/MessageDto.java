package chess.websocket.commander.dto;

public class MessageDto {
    private String sender;
    private String contents;

    public MessageDto() {
    }

    public MessageDto(String sender, String contents) {
        this.sender = sender;
        this.contents = contents;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
