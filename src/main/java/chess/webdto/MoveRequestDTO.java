package chess.webdto;

import org.springframework.stereotype.Component;

@Component
public class MoveRequestDTO {
    private  String start;
    private  String destination;

    public void setStart(String start) {
        this.start = start;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }
}
