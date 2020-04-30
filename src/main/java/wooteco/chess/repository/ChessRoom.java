package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("chess_room")
public class ChessRoom {
    @Id
    @Column("room_id")
    private Long roomId;
    private String roomName;
    private Set<MoveCommand> moveCommand = new HashSet<>();
//    @MappedCollection(idColumn="command_id", keyColumn="command_key")
//    private List<MoveCommand> moveCommand =  new ArrayList<>();

    public ChessRoom() {
    }

    public ChessRoom(String roomName) {
        this.roomName = roomName;
    }

    public void addCommand(MoveCommand command) {
        moveCommand.add(command);
    }

    public void removeCommand(MoveCommand commands) {
        moveCommand.remove(commands);
    }

    public Set<MoveCommand> getMoveCommand() {
        return moveCommand;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
