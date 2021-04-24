package chess.controller.dto;

public class GameDto {
    private final Long id;
    private final String name;

    public GameDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
