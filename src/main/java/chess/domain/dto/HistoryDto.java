package chess.domain.dto;

public class HistoryDto {

    private final String id;
    private final String name;

    public HistoryDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
