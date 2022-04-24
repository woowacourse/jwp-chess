package chess.dto;

import chess.domain.member.Member;
import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardDto {

    private final List<List<PieceDto>> boardSymbols;
    private final String roomTitle;
    private final String whiteMemberName;
    private final String blackMemberName;

    private BoardDto(List<List<PieceDto>> boardSymbols, String roomTitle, String whiteMemberName, String blackMemberName) {
        this.boardSymbols = boardSymbols;
        this.roomTitle = roomTitle;
        this.whiteMemberName = whiteMemberName;
        this.blackMemberName = blackMemberName;
    }

    public static BoardDto of(Map<String, Piece> pieces, String roomName, Member whiteMember, Member blackMember) {
        List<List<PieceDto>> boardSymbols = new ArrayList<>();
        for (Integer row : Row.valuesByDescending()) {
            boardSymbols.add(makeLine(pieces, row));
        }
        return new BoardDto(boardSymbols, roomName, whiteMember.getName(), blackMember.getName());
    }

    private static List<PieceDto> makeLine(Map<String, Piece> pieces, Integer row) {
        List<PieceDto> symbols = new ArrayList<>();
        for (Column column : Column.values()) {
            symbols.add(PieceDto.of(ViewConverter.findImageName(pieces, row, column), column.name() + row, ViewConverter.findBackground(row, column)));
        }
        return symbols;
    }

    public List<List<PieceDto>> getBoardSymbols() {
        return boardSymbols;
    }

    public String getWhiteMemberName() {
        return whiteMemberName;
    }

    public String getBlackMemberName() {
        return blackMemberName;
    }

    public String getRoomTitle() {
        return roomTitle;
    }
}
