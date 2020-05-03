package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table("chess_room")
public class ChessRoom {
    @Id
    private Long roomId;
    private String roomName;
    private List<MoveCommand> moveCommand = new ArrayList<>();

    protected ChessRoom() {
    }

    public ChessRoom(String roomName) {
        this.roomName = roomName;
    }

    public void addCommand(MoveCommand command) {
        moveCommand.add(command);
    }

    public List<MoveCommand> getMoveCommand() {
        return moveCommand;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
