package chess.dto;

import chess.model.member.Member;

import java.util.List;

public class RoomDto {

    private final int roomId;
    private final String roomTitle;
    private final String whiteMember;
    private final String blackMember;

    private final String gameStatus;

    public RoomDto(int roomId, String roomTitle, List<Member> members, String gameStatus) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.whiteMember = members.get(0).getName();
        this.blackMember = members.get(1).getName();
        this.gameStatus = gameStatus;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getWhiteMember() {
        return whiteMember;
    }

    public String getBlackMember() {
        return blackMember;
    }

    public String getGameStatus() {
        return gameStatus;
    }
}
