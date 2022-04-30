package chess.dto;

import chess.domain.event.MoveRoute;

public class MoveRouteDto {

    private String source;
    private String target;

    public MoveRouteDto() {
    }

    public MoveRouteDto(String source, String target) {
        this.source = source;
        this.target = target;
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
