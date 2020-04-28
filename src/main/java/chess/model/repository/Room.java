package chess.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ROOM_TB")
public class Room {

    @Id
    private Integer id;
    @Column("NM")
    private String name;
    @Column("PW")
    private String password;
    private String usedYN;

    public Room() {
    }

    public Room(String name, String password) {
        this(name, password, "Y");
    }

    public Room(String name, String password, String usedYN) {
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
