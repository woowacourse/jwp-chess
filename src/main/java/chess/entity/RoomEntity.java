package chess.entity;

public class RoomEntity {

    private static final Long EMPTY_ID = 0L;

    private Long id;
    private String name;
    private String password;

    public RoomEntity(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public RoomEntity(String name, String password) {
        this(EMPTY_ID, name, password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
