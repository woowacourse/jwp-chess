package chess.dto;

import chess.model.member.Member;

import java.util.List;

public class RoomDto {

    private final int roomId;
    private final String roomTitle;
    private final String password;
    private final String whiteMember;
    private final String blackMember;
    private final boolean end;

    public RoomDto(int roomId, String roomTitle, String password, List<Member> members, boolean end) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.password = password;
        this.whiteMember = members.get(0).getName();
        this.blackMember = members.get(1).getName();
        this.end = end;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getPassword() {
        return password;
    }

    public String getWhiteMember() {
        return whiteMember;
    }

    public String getBlackMember() {
        return blackMember;
    }

    public boolean isEnd() {
        return end;
    }
}
