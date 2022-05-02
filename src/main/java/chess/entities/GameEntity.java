package chess.entities;

import chess.domain.Game;
import chess.domain.position.Position;
import java.util.List;

public final class GameEntity {

    private final Integer id;
    private final String roomTitle;
    private final List<MemberEntity> memberEntities;
    private final String password;
    private final Game game;

    public GameEntity(Integer id, String roomTitle, List<MemberEntity> memberEntities, String password,
                      Game game) {
        this.id = id;
        this.roomTitle = roomTitle;
        this.memberEntities = memberEntities;
        this.password = password;
        this.game = game;
        validate(password);
    }

    public GameEntity(String title, List<MemberEntity> memberEntities, String password, Game game) {
        this(null, title, memberEntities, password, game);
    }

    public GameEntity(String title, List<MemberEntity> memberEntities, String password) {
        this(null, title, memberEntities, password, null);
    }

    public GameEntity(int id, String roomTitle, List<MemberEntity> allByBoardId, String password) {
        this(id, roomTitle, allByBoardId, password, null);
    }

    public GameEntity(int id, String room_title, String password, Game game) {
        this(id, room_title, null, password, game);
    }

    public GameEntity(int id, String roomTitle, String password) {
        this(id, roomTitle, null, password, null);
    }

    public GameEntity(String title, String password, Game game) {
        this(null, title, null, password, game);
    }

    public GameEntity(String title, String password) {
        this(null, title, null, password, null);
    }

    private void validate(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 필요합니다.");
        }
    }

    public void move(Position sourceRawPosition, Position targetRawPosition) {
        game.move(sourceRawPosition, targetRawPosition);
    }

    public int getId() {
        return id;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public List<MemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public String getPassword() {
        return password;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", roomTitle='" + roomTitle + '\'' +
                ", memberEntities=" + memberEntities +
                ", password='" + password + '\'' +
                ", game=" + game +
                '}';
    }
}
