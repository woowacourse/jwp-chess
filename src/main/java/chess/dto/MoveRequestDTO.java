package chess.dto;

import javax.validation.constraints.NotNull;

public class MoveRequestDTO {

    @NotNull
    private final String current;
    @NotNull
    private final String destination;
    @NotNull
    private final String teamType;

    public MoveRequestDTO(String current, String destination, String teamType) {
        this.current = current;
        this.destination = destination;
        this.teamType = teamType;
    }

    public String getCurrent() {
        return current;
    }

    public String getDestination() {
        return destination;
    }

    public String getTeamType() {
        return teamType;
    }
}
