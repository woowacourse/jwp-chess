package chess.model.repository;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ROOM_TB")
public final class RoomEntity {

    private static final String DEFAULT_USED_YN = "Y";

    @Id
    private Integer id;
    @Column("NM")
    private String name;
    @Column("PW")
    private String password;
    private String usedYN;
    private Set<ChessGameEntity> chessGameEntities = new HashSet<>();

    protected RoomEntity() {
    }

    public RoomEntity(String name, String password) {
        this(name, password, DEFAULT_USED_YN);
    }

    public RoomEntity(String name, String password, String usedYN) {
        this.name = name;
        this.password = password;
        this.usedYN = usedYN;
    }

    public void addGame(ChessGameEntity chessGameEntity) {
        chessGameEntities.add(chessGameEntity);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsedYN() {
        return usedYN;
    }

    public void setUsedYN(String usedYN) {
        this.usedYN = usedYN;
    }
}
