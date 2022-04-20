package springchess.dto;

import springchess.model.member.Member;
import springchess.model.piece.Piece;
import springchess.model.square.File;
import springchess.model.square.Rank;
import springchess.model.square.Square;

import java.util.*;

public class BoardDto {

    private final List<List<PieceDto>> dto;
    private final String roomTitle;
    private final String whiteMemberName;
    private final String blackMemberName;

    private BoardDto(List<List<PieceDto>> dto, String roomTitle, String whiteMemberName,
                     String blackMemberName) {
        this.dto = dto;
        this.roomTitle = roomTitle;
        this.whiteMemberName = whiteMemberName;
        this.blackMemberName = blackMemberName;
    }

    public static BoardDto of(Map<Square, Piece> pieces, String roomName, Member whiteMember, Member blackMember) {
        List<List<PieceDto>> boardDto = new ArrayList<>();
        List<Rank> ranks = Arrays.asList(Rank.values());
        Collections.reverse(ranks);
        for (Rank rank : ranks) {
            boardDto.add(makeLineByFile(pieces, rank));
        }
        return new BoardDto(boardDto, roomName, whiteMember.getName(), blackMember.getName());
    }

    private static List<PieceDto> makeLineByFile(Map<Square, Piece> pieces, Rank rank) {
        List<PieceDto> tempLine = new ArrayList<>();
        for (File file : File.values()) {
            Piece piece = pieces.get(Square.of(file, rank));
            tempLine.add(PieceDto.of(piece, file, rank));
        }
        return tempLine;
    }

    public List<List<PieceDto>> getDto() {
        return dto;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getWhiteMemberName() {
        return whiteMemberName;
    }

    public String getBlackMemberName() {
        return blackMemberName;
    }
}
