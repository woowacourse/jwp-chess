package chess.dto;

import chess.domain.event.MoveRoute;

public class MoveRouteDto {

    private int id;
    private String source;
    private String target;

    public MoveRouteDto() {
    }

    public MoveRouteDto(int id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }


    public MoveRoute toMoveRoute() {
        return new MoveRoute(source, target);
    }

    @Override
    public String toString() {
        return "MoveRouteDto{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
