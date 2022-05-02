package chess.controller.request;

public class RoomCreationRequest {

    private String title;
    private String password;

    public RoomCreationRequest(){
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
