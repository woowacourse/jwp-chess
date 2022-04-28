package chess.dto.request;

import chess.domain.event.MoveRoute;

public class MoveRouteDto {

    private String source;
    private String target;

    public MoveRouteDto() {
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public MoveRoute toDomain() {
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
