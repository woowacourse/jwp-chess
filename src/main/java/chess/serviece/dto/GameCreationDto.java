package chess.serviece.dto;

public class GameCreationDto {

    private String title;
    private String password;

    public GameCreationDto(){
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
