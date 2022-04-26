package chess.dto;

public final class GameCreationDTO {

    private final String name;
    private final String password;

    public GameCreationDTO(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
