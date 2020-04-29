package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Commands")
public class Commands {
    @Id
    @Column("CommandID")
    private Long commandId;

    @Column("Command")
    private String command;
    private ChessRoomRef chessRoom;

    public Commands(String command) {
        this.command = command;
    }

    public Commands(String command, ChessRoom chessRoom) {
        this.command = command;
        this.chessRoom = new ChessRoomRef(chessRoom);
    }

    public Long getCommandId() {
        return commandId;
    }

    public String getCommand() {
        return command;
    }

    public ChessRoomRef getChessRoom() {
        return chessRoom;
    }
}