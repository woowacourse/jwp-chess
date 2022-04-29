package chess.database.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomDto {

    private int id;
    private String name;
    private String password;

    public RoomDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public RoomDto(@JsonProperty("name") String name, @JsonProperty("password") String password) {
        this.name = name;
        this.password = password;
    }

    public RoomDto(int id, String name, String password) {
        this(name, password);
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
