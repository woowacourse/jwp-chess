package chess.domain.game;

import chess.domain.member.Member;
import chess.domain.pieces.Color;

import java.util.ArrayList;
import java.util.List;

public final class ChessBoard {

    private final Integer id;
    private final String roomTitle;
    private final Color turn;
    private final List<Member> members;
    private final String password;

    public ChessBoard(Integer id, String roomTitle, Color turn, List<Member> members, String password) {
        this.id = id;
        this.roomTitle = roomTitle;
        this.turn = turn;
        this.members = members;
        this.password = password;
    }

    public ChessBoard(Integer roomId, String roomTitle, Color turn) {
        this(roomId, roomTitle, turn, new ArrayList<>(), null);
    }

    public ChessBoard(String roomTitle, Color turn, List<Member> members, String password) {
        this(null, roomTitle, turn, members, password);
    }

    public ChessBoard(String roomTitle, String password) {
        this(null, roomTitle, Color.WHITE, new ArrayList<>(), password);
    }

    public int getId() {
        return id;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public Color getTurn() {
        return turn;
    }

    public List<Member> getMembers() {
        return members;
    }

    public String getPassword() {
        return password;
    }
}
