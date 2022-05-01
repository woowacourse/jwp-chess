package chess.entities;

import chess.domain.pieces.Color;

import java.util.ArrayList;
import java.util.List;

public final class GameEntity {

    private final Integer id;
    private final String roomTitle;
    private final Color turn;
    private final List<MemberEntity> memberEntities;
    private final String password;

    public GameEntity(Integer id, String roomTitle, Color turn, List<MemberEntity> memberEntities, String password) {
        this.id = id;
        this.roomTitle = roomTitle;
        this.turn = turn;
        this.memberEntities = memberEntities;
        this.password = password;
        validate(password);
    }

    private void validate(String password) {
        if(password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 필요합니다.");
        }
    }

    public GameEntity(Integer roomId, String roomTitle, Color turn, String password) {
        this(roomId, roomTitle, turn, new ArrayList<>(), password);
    }

    public GameEntity(String roomTitle, Color turn, List<MemberEntity> memberEntities, String password) {
        this(null, roomTitle, turn, memberEntities, password);
    }

    public GameEntity(String roomTitle, Color turn, String password) {
        this(null, roomTitle, turn, new ArrayList<>(), password);
    }

    public GameEntity(String roomTitle, String password) {
        this(null, roomTitle, Color.WHITE, password);
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

    public List<MemberEntity> getMembers() {
        return memberEntities;
    }

    public String getPassword() {
        return password;
    }
}
