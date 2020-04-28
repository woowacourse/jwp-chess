package chess.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ROOM_TB")
public class RoomEntity {

    @Id
    private Integer id;
    @Column("NM")
    private String name;
    @Column("PW")
    private String password;
    private String usedYN;

    public RoomEntity() {
    }

    public RoomEntity(String name, String password) {
        this(name, password, "Y");
    }

    public RoomEntity(String name, String password, String usedYN) {
        this.name = name;
        this.password = password;
        this.usedYN = usedYN;
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
