package chess.dto.response;

import chess.entities.MemberEntity;

public class RoomDto {

    private final int roomId;
    private final String roomTitle;
    private final String whiteMember;
    private final String blackMember;

    public RoomDto(int roomId, String roomTitle, MemberEntity whiteMemberEntity, MemberEntity blackMemberEntity) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.whiteMember = whiteMemberEntity.getName();
        this.blackMember = blackMemberEntity.getName();
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
}
