package chess.websocket;

import chess.websocket.util.Command;
import java.util.Map;

public class RequestForm {
    private Command command;
    private Map<String, Object> data;

    public RequestForm() {
    }

    public RequestForm(Command command, Map<String, Object> data) {
        this.command = command;
        this.data = data;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
