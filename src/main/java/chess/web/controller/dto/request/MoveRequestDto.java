package chess.web.controller.dto.request;


public class MoveRequestDto {

    private Long gameId;
    private String startPositionInput;
    private String destinationInput;
    private String encryptedPassword;

    public Long getGameId() {
        return gameId;
    }

    public String getStartPositionInput() {
        return startPositionInput;
    }

    public String getDestinationInput() {
        return destinationInput;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
