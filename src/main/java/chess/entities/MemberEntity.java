package chess.entities;

public class MemberEntity {

    private final Integer id;
    private final String name;
    private final Integer boardId;

    public MemberEntity(Integer id, String name, Integer boardId) {
        this.id = id;
        this.name = name;
        this.boardId = boardId;
    }

    public MemberEntity(String name) {
        this(null, name, null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBoardId() {
        return boardId;
    }
}
